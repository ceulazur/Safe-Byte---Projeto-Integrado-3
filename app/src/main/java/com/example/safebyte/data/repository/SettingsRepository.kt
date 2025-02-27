package com.example.safebyte.data.repository

import com.example.safebyte.data.local.Settings
import com.example.safebyte.data.local.SettingsDao
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDao: SettingsDao) {
    suspend fun insertSettings(settings: Settings) {
        settingsDao.insertSettings(settings)
    }

    suspend fun updateSettings(settings: Settings) {
        settingsDao.updateSettings(settings)
    }

    suspend fun getSettings(): Flow<Settings?> {
        return settingsDao.getSettings()
    }
}































