package com.example.safebyte.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val id: Int = 0,
    val darkThemeEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val animationsEnabled: Boolean = true
)