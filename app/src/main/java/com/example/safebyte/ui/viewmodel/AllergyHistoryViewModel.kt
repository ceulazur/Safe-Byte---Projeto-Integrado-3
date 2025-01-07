package com.example.safebyte.ui.viewmodel


import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebyte.data.model.AllergyHistoryItem
import com.example.safebyte.data.repository.AllergyHistoryRepository
import kotlinx.coroutines.launch
import java.util.UUID

sealed class UploadState {
    object Initial : UploadState()
    data class Uploading(val progress: Float) : UploadState()
    data class Success(val downloadUrl: String) : UploadState()
    data class Error(val message: String) : UploadState()
}

class AllergyHistoryViewModel(
    private val repository: AllergyHistoryRepository = AllergyHistoryRepository()
) : ViewModel() {
    private val _allergyHistoryState = mutableStateOf<List<AllergyHistoryItem>>(emptyList())
    val allergyHistoryState: State<List<AllergyHistoryItem>> = _allergyHistoryState

    private val _uploadState = mutableStateOf<UploadState>(UploadState.Initial)
    val uploadState: State<UploadState> = _uploadState

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
                        _uploadState.value = UploadState.Error(exception.message ?: "Erro desconhecido")
                    }
                )
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Erro desconhecido")
            }
        }
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