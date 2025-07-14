package com.carissa.revibes.transaction_history.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TransactionDetailResponse(
    val code: Int,
    val message: String,
    val data: TransactionDetailData,
    val status: String
)

@Keep
@Serializable
data class TransactionDetailData(
    val id: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val country: String,
    val address: String,
    val addressDetail: String?,
    val postalCode: String,
    val storeLocation: String,
    val items: List<TransactionDetailItemData>,
    val status: String,
    val totalPoint: Int
)

@Keep
@Serializable
data class TransactionDetailItemData(
    val id: String,
    val name: String,
    val type: String,
    val weight: Int,
    val point: Int,
    val media: List<TransactionDetailMediaData>
)

@Keep
@Serializable
data class TransactionDetailMediaData(
    val uploadUrl: String,
    val downloadUri: String,
    val expiredAt: Long
)
