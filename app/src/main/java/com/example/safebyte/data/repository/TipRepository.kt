package com.example.safebyte.data.repository

import com.example.safebyte.data.ClientSupabase
import com.example.safebyte.data.model.Tip
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class TipRepository {
    companion object {
        private const val TABLE_NAME = "tips"
    }

//    suspend fun insertTip(tip: Tip): Result<Tip> =
//        ClientSupabase.withSupabase { client ->
//            client.from(TABLE_NAME)
//                .insert(tip) { select() }
//                .decodeAs<Tip>()
//        }

    suspend fun getLastTip(): Result<Tip?> =
        ClientSupabase.withSupabase { client ->
            client.from(TABLE_NAME)
                .select {
                    order("created_at", order = Order.DESCENDING)
                    limit(1)
                }
                .decodeList<Tip>()
                .firstOrNull()
        }

//    suspend fun getAllTips(): Result<List<Tip>> =
//        ClientSupabase.withSupabase { client ->
//            client.from(TABLE_NAME)
//                .select()
//                .decodeList<Tip>()
//        }
}
