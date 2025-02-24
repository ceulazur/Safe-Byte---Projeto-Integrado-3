package com.example.safebyte.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimelineEvent(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("date")
    val date: Long,
    @SerialName("activitieslist")
    val activitieslist: List<String>,
    @SerialName("videourl")
    val videourl: String? = null,
)