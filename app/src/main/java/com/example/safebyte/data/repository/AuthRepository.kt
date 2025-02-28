package com.example.safebyte.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    suspend fun login(email: String, password: String): Boolean {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            throw Exception(e.message ?: "Erro desconhecido ao fazer login.")
        }
    }

    suspend fun loginWithGoogle(credential: AuthCredential): Boolean {
        return try {
            firebaseAuth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            throw Exception(e.message ?: "Erro ao autenticar com o Google.")
        }
    }

    suspend fun register(email: String, password: String): Boolean {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            throw Exception(e.message ?: "Erro ao criar conta.")
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
