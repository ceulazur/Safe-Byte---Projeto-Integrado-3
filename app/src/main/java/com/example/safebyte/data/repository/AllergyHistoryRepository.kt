package com.example.safebyte.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.safebyte.data.ClientSupabase
import com.example.safebyte.data.model.AllergyHistoryItem
import com.example.safebyte.data.model.TimelineEvent
import com.google.firebase.storage.FirebaseStorage
import io.github.jan.supabase.postgrest.from
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
        ), AllergyHistoryItem(
            id = "2", allergen = "Shellfish", severity = "Medium", date = "2024-02-20"
        ), AllergyHistoryItem(
            id = "3",
            allergen = "Latex",
            severity = "Low",
            date = "2024-03-10",
            notes = "Mild skin irritation"
        )
    )

    companion object {
        private const val TABLE_NAME = "allergy_history"
    }

    suspend fun uploadVideo(
        uri: Uri,
        fileName: String,
        context: Context,
        onProgress: (Float) -> Unit,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        try {
            val videoRef = videosRef.child(fileName)

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val uploadTask = videoRef.putStream(inputStream)

                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress =
                        taskSnapshot.bytesTransferred.toFloat() / taskSnapshot.totalByteCount.toFloat()
                    onProgress(progress)
                }.addOnFailureListener { exception ->
                    onError(exception)
                }.await()

                val downloadUrl = videoRef.downloadUrl.await()
                onSuccess(downloadUrl.toString())
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

    suspend fun getAllergyHistory(): Result<List<TimelineEvent>> {
        return try {
            ClientSupabase.withSupabase { client ->
                client
                    .from(TABLE_NAME)
                    .select()
                    .decodeList<TimelineEvent>()
            }
        } catch (e: Exception) {
            Log.e("AllergyHistoryRepository", "Error fetching history: ${e.message}", e)
            Result.failure(e)
        }
    }


    suspend fun addAllergyHistoryItem(item: TimelineEvent) {
        val newItem = item.copy(id = null)

        try {
            ClientSupabase.withSupabase { client ->
                val response = client
                    .from(TABLE_NAME)
                    .insert(newItem)
                    .decodeAs<TimelineEvent>()

                Log.d("AllergyHistoryRepository", "Success! Response: $response")
            }
        } catch (e: Exception) {
            Log.e("AllergyHistoryRepository", "Error inserting item: ${e.message}", e)
            throw e
        }
    }

    fun removeAllergyHistoryItem(id: String) {
        _allergyHistoryItems.removeIf { it.id == id }
    }
}