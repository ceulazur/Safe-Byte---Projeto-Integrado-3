package com.example.safebyte.ui.viewmodel


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.safebyte.data.model.AllergyHistoryItem
import com.example.safebyte.data.repository.AllergyHistoryRepository

class AllergyHistoryViewModel(
    private val repository: AllergyHistoryRepository = AllergyHistoryRepository()
) : ViewModel() {
    private val _allergyHistoryState = mutableStateOf<List<AllergyHistoryItem>>(emptyList())
    val allergyHistoryState: State<List<AllergyHistoryItem>> = _allergyHistoryState

    init {
        fetchAllergyHistory()
    }

    private fun fetchAllergyHistory() {
        _allergyHistoryState.value = repository.getAllergyHistory()
    }

    fun addAllergyHistoryItem(item: AllergyHistoryItem) {
        repository.addAllergyHistoryItem(item)
        fetchAllergyHistory()
    }

    fun removeAllergyHistoryItem(id: String) {
        repository.removeAllergyHistoryItem(id)
        fetchAllergyHistory()
    }
}