package com.example.safebyte.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebyte.data.model.AllergyHistoryItem
import com.example.safebyte.data.model.TimelineEvent
import com.example.safebyte.data.repository.AllergyHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

sealed class UploadState {
    data object Initial : UploadState()
    data class Uploading(val progress: Float) : UploadState()
    data class Success(val downloadUrl: String) : UploadState()
    data class Error(val message: String) : UploadState()
}

sealed class AllergyHistoryState {
    data object Loading : AllergyHistoryState()
    data class Success(val items: List<AllergyHistoryItem>) : AllergyHistoryState()
    data class Error(val message: String) : AllergyHistoryState()
}

class AllergyHistoryViewModel(
    private val repository: AllergyHistoryRepository = AllergyHistoryRepository(),
) : ViewModel() {
    private val _allergyHistoryState = mutableStateOf<List<TimelineEvent>>(emptyList())
    val allergyHistoryState: State<List<TimelineEvent>> = _allergyHistoryState

    private val _uploadState = mutableStateOf<UploadState>(UploadState.Initial)
    val uploadState: State<UploadState> = _uploadState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data class ShowSuccess(val message: String) : UiEvent()
    }

    init {
        fetchAllergyHistory()
    }

    fun uploadVideo(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                _uploadState.value = UploadState.Uploading(0f)

                val fileName = "video_${UUID.randomUUID()}.mp4"

                repository.uploadVideo(uri, fileName, context,
                    onProgress = { progress ->
                        _uploadState.value = UploadState.Uploading(progress)
                    },
                    onSuccess = { downloadUrl ->
                        _uploadState.value = UploadState.Success(downloadUrl)
                    },
                    onError = { exception ->
                        _uploadState.value =
                            UploadState.Error(exception.message ?: "Erro desconhecido")
                    }
                )
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    private fun fetchAllergyHistory() {
        viewModelScope.launch {
            _allergyHistoryState.value = repository.getAllergyHistory().getOrDefault(emptyList())
        }
    }

    suspend fun addAllergyHistoryItem(item: TimelineEvent): Boolean {
        Log.d("AllergyHistoryViewModel", "Adicionando item: $item")
        return withContext(Dispatchers.IO) {
            try {
                repository.addAllergyHistoryItem(item)
                fetchAllergyHistory()
                _uiEvent.emit(UiEvent.ShowSuccess("Item adicionado com sucesso"))

                val updatedHistory = repository.getAllergyHistory().getOrDefault(emptyList())
                _allergyHistoryState.value = updatedHistory


                true
            } catch (e: Exception) {
                Log.e("AllergyHistoryViewModel", "Erro ao adicionar item", e)
                _uiEvent.emit(UiEvent.ShowError(e.message ?: "Erro ao adicionar item"))
                false
            }
        }
    }


    fun removeAllergyHistoryItem(id: String) {
        repository.removeAllergyHistoryItem(id)
        fetchAllergyHistory()
    }
}