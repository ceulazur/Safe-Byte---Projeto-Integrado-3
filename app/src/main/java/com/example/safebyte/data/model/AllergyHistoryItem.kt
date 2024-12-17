package com.example.safebyte.data.model

data class AllergyHistoryItem(
    val id: String,
    val allergen: String,
    val severity: String,
    val date: String,
    val notes: String? = null
)