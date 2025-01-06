package com.example.safebyte.data.repository

import android.content.Context
import android.net.Uri
import com.example.safebyte.data.model.AllergyHistoryItem
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AllergyHistoryRepository {
    private val storage by lazy { FirebaseStorage.getInstance() }
    private val videosRef by lazy { storage.reference.child("videos") }

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

    suspend fun uploadVideo(
        uri: Uri,
        fileName: String,
        context: Context,
        onProgress: (Float) -> Unit,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val videoRef = videosRef.child(fileName)

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val uploadTask = videoRef.putStream(inputStream)

                uploadTask
                    .addOnProgressListener { taskSnapshot ->
                        val progress = taskSnapshot.bytesTransferred.toFloat() / taskSnapshot.totalByteCount.toFloat()
                        onProgress(progress)
                    }
                    .addOnFailureListener { exception ->
                        onError(exception)
                    }
                    .await()

                val downloadUrl = videoRef.downloadUrl.await()
                onSuccess(downloadUrl.toString())
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

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