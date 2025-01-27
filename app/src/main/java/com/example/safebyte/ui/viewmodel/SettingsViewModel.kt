package com.example.safebyte.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Define the DataStore at the package level
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsViewModel : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

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
    }
}