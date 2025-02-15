package com.example.safebyte.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tip(
    val id: Int? = null,
    val message: String,
    @SerialName("created_at")
    val createdAt: Instant
)
