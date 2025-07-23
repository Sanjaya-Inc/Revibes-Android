package com.carissa.revibes.exchange_points.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerialName("limit")
    val limit: Int,
    @SerialName("sortBy")
    val sortBy: String,
    @SerialName("sortOrder")
    val sortOrder: String,
    @SerialName("lastDocId")
    val lastDocId: String,
    @SerialName("firstDocId")
    val firstDocId: String,
    @SerialName("direction")
    val direction: String,
    @SerialName("hasMoreNext")
    val hasMoreNext: Boolean,
    @SerialName("hasMorePrev")
    val hasMorePrev: Boolean
)
