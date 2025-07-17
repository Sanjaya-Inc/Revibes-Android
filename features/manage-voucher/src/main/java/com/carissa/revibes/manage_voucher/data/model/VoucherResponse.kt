package com.carissa.revibes.manage_voucher.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class VoucherListResponse(
    val success: Boolean,
    val message: String,
    val data: VoucherListData
)

@Serializable
@Keep
data class VoucherListData(
    val items: List<VoucherData>,
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
data class VoucherConditionsData(
    val maxClaim: Int,
    val maxUsage: Int,
    val minOrderItem: Int,
    val minOrderAmount: Double,
    val maxDiscountAmount: Double
)

@Serializable
@Keep
data class PaginationData(
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasMoreNext: Boolean,
    val hasMorePrev: Boolean,
    val lastDocId: String?
)
