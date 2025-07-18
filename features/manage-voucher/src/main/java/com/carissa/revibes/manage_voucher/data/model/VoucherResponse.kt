package com.carissa.revibes.manage_voucher.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class VoucherListResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: VoucherListData
)

@Serializable
@Keep
data class VoucherListData(
    val items: List<VoucherItemData>,
    val pagination: PaginationData
)

@Serializable
@Keep
data class VoucherDetailResponse(
    val success: Boolean,
    val message: String,
    val data: VoucherData
)

@Serializable
@Keep
data class VoucherData(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val type: String,
    val amount: Double,
    val currency: String,
    val conditions: VoucherConditionsData,
    val claimPeriodStart: String,
    val claimPeriodEnd: String,
    val imageUrl: String? = null,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
@Keep
data class VoucherItemData(
    val id: String,
    val code: String,
    val name: String,
    val value: VoucherValue,
    val imageUri: String,
    val claimPeriodStart: String,
    val claimPeriodEnd: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
@Keep
data class VoucherValue(
    val type: String,
    val amount: Long,
)

@Serializable
@Keep
data class VoucherConditionsData(
    val maxClaim: Int,
    val maxUsage: Int,
    val minOrderItem: Int,
    val minOrderAmount: Long,
    val maxDiscountAmount: Long,
)

@Serializable
@Keep
data class PaginationData(
    val limit: Int,
    val sortBy: String,
    val sortOrder: String,
    val lastDocId: String? = null,
    val firstDocId: String? = null,
    val direction: String,
    val hasMoreNext: Boolean,
    val hasMorePrev: Boolean
)
