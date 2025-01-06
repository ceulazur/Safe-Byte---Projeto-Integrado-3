package com.example.safebyte.data.repository

import com.example.safebyte.data.model.AllergyHistoryItem

class AllergyHistoryRepository {
    private val _allergyHistoryItems = mutableListOf(
        AllergyHistoryItem(
            id = "1",
            allergen = "Peanuts",
            severity = "High",
            date = "2024-01-15",
            notes = "Anaphylactic reaction"
        ),
        AllergyHistoryItem(
            id = "2",
            allergen = "Shellfish",
            severity = "Medium",
            date = "2024-02-20"
        ),
        AllergyHistoryItem(
            id = "3",
            allergen = "Latex",
            severity = "Low",
            date = "2024-03-10",
            notes = "Mild skin irritation"
        )
    )

    fun getAllergyHistory(): List<AllergyHistoryItem> {
        return _allergyHistoryItems
    }

    fun addAllergyHistoryItem(item: AllergyHistoryItem) {
        _allergyHistoryItems.add(item)
    }

    fun removeAllergyHistoryItem(id: String) {
        _allergyHistoryItems.removeIf { it.id == id }
    }
}