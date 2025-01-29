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
import com.example.safebyte.utils.NotificationReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsViewModel : ViewModel() {
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

    fun toggleNotifications(context: Context, enabled: Boolean) {
        viewModelScope.launch {
            _isNotificationEnabled.value = enabled

            context.dataStore.edit { preferences ->
                preferences[NOTIFICATION_KEY] = enabled
            }

            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            prefs.edit().putBoolean("notifications_enabled", enabled).apply()

            if (enabled) {
                scheduleNotification(context)
            } else {
                cancelNotification(context)
            }
        }
    }

    fun loadNotificationState(context: Context) {
        viewModelScope.launch {
            context.dataStore.data
                .map { preferences ->
                    preferences[NOTIFICATION_KEY] ?: false
                }
                .collect { enabled ->
                    _isNotificationEnabled.value = enabled
                }
        }
    }

    fun scheduleNotification(context: Context) {
        try {
            Log.d("NotificationScheduler", "Starting notification scheduling")

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = "DAILY_NOTIFICATION"
            }

            Log.d("NotificationScheduler", "Creating PendingIntent")
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            Log.d("NotificationScheduler", "Intent action: ${intent.action}")

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.HOUR_OF_DAY, 3)
            }

            Log.d("NotificationScheduler", "Scheduled time: ${calendar.time}")
            Log.d("NotificationScheduler", "Current time: ${Date()}")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    Log.d("NotificationScheduler", "Can schedule exact alarms")
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    Log.e(
                        "NotificationScheduler",
                        "Cannot schedule exact alarms - permission not granted"
                    )

                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)

                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("NotificationScheduler", "Using setAlarmClock for older Android versions")
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent),
                    pendingIntent
                )
            } else {
                Log.d("NotificationScheduler", "Using setRepeating for very old Android versions")
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }

            Log.d("NotificationScheduler", "Notification scheduled successfully")
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