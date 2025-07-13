package com.carissa.revibes.transaction_history.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TransactionHistoryResponse(
    val code: Int,
    val message: String,
    val data: TransactionHistoryResponseData,
    val status: String
)

@Keep
@Serializable
data class TransactionHistoryResponseData(
    val items: List<LogisticOrderData>,
    val pagination: PaginationData
)

@Keep
@Serializable
data class LogisticOrderData(
    val id: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val country: String,
    val address: String,
    val postalCode: String,
    val storeLocation: String?,
    val status: String,
    val maker: String,
    val items: List<LogisticItemData>,
    val totalPoint: Int
)

@Keep
@Serializable
data class LogisticItemData(
    val id: String,
    val name: String,
    val type: String,
    val weight: Int,
    val point: Int,
    val media: List<MediaData>
)

@Keep
@Serializable
data class MediaData(
    val uploadUrl: String,
    val downloadUri: String,
    val expiredAt: Long
)

@Keep
@Serializable
data class PaginationData(
    val limit: Int,
    val sortBy: String,
    val sortOrder: String,
    val lastDocId: String?,
    val firstDocId: String?,
    val direction: String,
    val hasMoreNext: Boolean,
    val hasMorePrev: Boolean
)
