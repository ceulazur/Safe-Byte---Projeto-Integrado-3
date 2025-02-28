package com.example.safebyte.viewmodel

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

class StorageUtil {
    fun uploadToStorage(fileUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("uploads/${fileUri.lastPathSegment}")
        val uploadTask = fileRef.putFile(fileUri)

        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }.addOnFailureListener {
                onFailure(it)
            }
        }.addOnFailureListener {
            onFailure(it)
        }
    }
}