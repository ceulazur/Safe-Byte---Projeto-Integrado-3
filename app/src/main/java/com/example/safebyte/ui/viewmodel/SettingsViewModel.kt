package com.example.safebyte.ui.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import com.example.safebyte.data.local.AppDatabase
import com.example.safebyte.data.repository.SettingsRepository
import com.example.safebyte.receiver.NotificationReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlinx.coroutines.flow.firstOrNull

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private val _isNotificationEnabled = MutableStateFlow(false)
    val isNotificationEnabled: StateFlow<Boolean> = _isNotificationEnabled

    private val _isAnimationsEnabled = MutableStateFlow(true)
    val isAnimationsEnabled: StateFlow<Boolean> = _isAnimationsEnabled

    init {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            loadThemeState(context)
            loadNotificationState(context)
            loadAnimationState(context)
        }
    }

    fun toggleAnimations(context: Context, enabled: Boolean) {
        viewModelScope.launch {
            try {
                val repository = getRepository(context)
                val currentSettings = repository.getSettings().firstOrNull()

                val newSettings = currentSettings?.copy(animationsEnabled = enabled) ?: com.example.safebyte.data.local.Settings(
                    id = 0,
                    animationsEnabled = enabled,
                    darkThemeEnabled = _isDarkTheme.value,
                    notificationsEnabled = _isNotificationEnabled.value
                )

                repository.insertSettings(newSettings)
                _isAnimationsEnabled.value = enabled
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error toggling animations", e)
            }
        }
    }

    fun loadAnimationState(context: Context) {
        viewModelScope.launch {
            try {
                getRepository(context).getSettings().collect { settings ->
                    _isAnimationsEnabled.value = settings?.animationsEnabled ?: true
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error loading animations", e)
            }
        }
    }

    fun toggleNotifications(context: Context, enabled: Boolean) {
        viewModelScope.launch {
            try {
                val repository = getRepository(context)
                val currentSettings = repository.getSettings().firstOrNull()

                val newSettings = currentSettings?.copy(notificationsEnabled = enabled) ?: com.example.safebyte.data.local.Settings(
                    id = 0,
                    notificationsEnabled = enabled,
                    darkThemeEnabled = _isDarkTheme.value,
                    animationsEnabled = _isAnimationsEnabled.value
                )

                repository.insertSettings(newSettings)
                _isNotificationEnabled.value = enabled

                if (enabled) {
                    scheduleNotification(context)
                } else {
                    cancelNotification(context)
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving notification preference", e)
            }
        }
    }

    fun toggleTheme(isDark: Boolean, context: Context) {
        viewModelScope.launch {
            try {
                val repository = getRepository(context)
                val currentSettings = repository.getSettings().firstOrNull()

                val newSettings = currentSettings?.copy(darkThemeEnabled = isDark) ?: com.example.safebyte.data.local.Settings(
                    id = 0,
                    darkThemeEnabled = isDark,
                    notificationsEnabled = _isNotificationEnabled.value,
                    animationsEnabled = _isAnimationsEnabled.value
                )

                repository.insertSettings(newSettings)
                _isDarkTheme.value = isDark
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error toggling theme", e)
            }
        }
    }

    fun loadNotificationState(context: Context) {
        viewModelScope.launch {
            try {
                getRepository(context).getSettings().collect { settings ->
                    val enabled = settings?.notificationsEnabled ?: false
                    _isNotificationEnabled.value = enabled

                    if (enabled) {
                        scheduleNotification(context)
                    } else {
                        cancelNotification(context)
                    }
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error loading notification preference", e)
            }
        }
    }

    fun loadThemeState(context: Context) {
        viewModelScope.launch {
            try {
                getRepository(context).getSettings().collect { settings ->
                    _isDarkTheme.value = settings?.darkThemeEnabled ?: isSystemInDarkMode(context)
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error loading theme", e)
            }
        }
    }

    private fun getRepository(context: Context): SettingsRepository {
        val db = AppDatabase.getDatabase(context)
        return SettingsRepository(db.settingsDao())
    }

    private fun saveAnimationState(context: Context, enabled: Boolean) {
        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("animations_enabled", enabled)
            apply()
        }
    }

    private fun scheduleNotification(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = "DAILY_NOTIFICATION"
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()

                set(Calendar.HOUR_OF_DAY, 13) // o tempo é 3 horas adiantado
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    val alarmIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(alarmIntent)
                }
            }

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent),
                pendingIntent
            )
        } catch (e: Exception) {
            Log.e("NotificationScheduler", "Error scheduling notification", e)
        }
    }

    private fun cancelNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun isSystemInDarkMode(context: Context): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}