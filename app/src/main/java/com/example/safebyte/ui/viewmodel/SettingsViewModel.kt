package com.example.safebyte.ui.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebyte.receiver.NotificationReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsViewModel : ViewModel() {
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private val _isNotificationEnabled = MutableStateFlow(false)
    val isNotificationEnabled: StateFlow<Boolean> = _isNotificationEnabled

    private val _isAnimationsEnabled = MutableStateFlow(true)
    val isAnimationsEnabled: StateFlow<Boolean> = _isAnimationsEnabled

    fun toggleAnimations(context: Context, enabled: Boolean) {
        _isAnimationsEnabled.value = enabled
        saveAnimationState(
            context, enabled
        )
    }

    fun loadAnimationState(context: Context) {
        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val animationsEnabled = sharedPref.getBoolean("animations_enabled", true)
        _isAnimationsEnabled.value = animationsEnabled
    }

    private fun saveAnimationState(context: Context, enabled: Boolean) {
        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("animations_enabled", enabled)
            apply()
        }
    }

//    fun fetchTips() {
//        viewModelScope.launch {
//            tipRepository.getAllTips()
//                .onSuccess { tips ->
//                    // Handle success
//                    _tips.value = tips
//                }
//                .onFailure { error ->
//                    // Handle error
//                    Log.e("SettingsviewModel", "Error fetching tips", error)
//                    _error.value = error.message
//                }
//        }
//    }

    fun toggleNotifications(context: Context, enabled: Boolean) {
        viewModelScope.launch {
            try {
                _isNotificationEnabled.value = enabled

                // Salva no DataStore
                context.dataStore.edit { preferences ->
                    preferences[NOTIFICATION_KEY] = enabled
                }

                // Agenda ou cancela a notificação
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

    fun loadNotificationState(context: Context) {
        viewModelScope.launch {
            try {
                context.dataStore.data
                    .map { preferences ->
                        preferences[NOTIFICATION_KEY] ?: false
                    }
                    .collect { enabled ->
                        _isNotificationEnabled.value = enabled
                        Log.d("SettingsViewModel", "Notification preference loaded: $enabled")

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

    fun toggleTheme(isDark: Boolean, context: Context) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[THEME_KEY] = isDark
            }
            _isDarkTheme.value = isDark
        }
    }

    fun loadThemeState(context: Context) {
        viewModelScope.launch {
            context.dataStore.data
                .map { preferences ->
                    preferences[THEME_KEY] ?: false
                }
                .collect { isDark ->
                    _isDarkTheme.value = isDark
                }
        }
    }

    companion object {
        val THEME_KEY = booleanPreferencesKey("dark_theme")
        val NOTIFICATION_KEY = booleanPreferencesKey("notifications_enabled")
    }
}