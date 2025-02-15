package com.example.safebyte.data

import android.util.Log
import com.example.safebyte.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ClientSupabase {
    @Volatile
    private var instance: SupabaseClient? = null

    private fun getInstance(): SupabaseClient {
        return instance ?: synchronized(this) {
            instance ?: createSupabaseClient(
                supabaseUrl = BuildConfig.SUPABASE_URL,
                supabaseKey = BuildConfig.SUPABASE_ANON_KEY
            ) {
                install(Postgrest)
                install(Auth) {
                    flowType = FlowType.PKCE
                    scheme = "app"
                    host = "supabase.com"
                }
                install(Storage)
            }.also { instance = it }
        }
    }

    suspend fun <T> withSupabase(block: suspend (SupabaseClient) -> T): Result<T> {
        return try {
            withContext(Dispatchers.IO) {
                // verificar conexao:
                val url = getInstance().supabaseHttpUrl
                Log.d("ClientSupabase", "Supabase URL: $url")

                val headerParams = getInstance().httpClient.toString()
                Log.d("ClientSupabase", "Header Params: $headerParams")

                Result.success(block(getInstance()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}