import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsViewModel : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    companion object {
        private val THEME_KEY = booleanPreferencesKey("dark_theme")
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
}