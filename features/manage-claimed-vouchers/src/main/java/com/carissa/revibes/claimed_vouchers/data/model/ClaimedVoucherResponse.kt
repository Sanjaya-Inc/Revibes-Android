package com.carissa.revibes.claimed_vouchers.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClaimedVoucherResponse(
    val code: Int,
    val message: String,
    val data: ClaimedVoucherDataResponse,
    val status: String
)

@Serializable
data class ClaimedVoucherDataResponse(
    val items: List<ClaimedVoucherItemResponse>,
    val pagination: PaginationResponse
)

@Serializable
data class ClaimedVoucherItemResponse(
    val id: String,
    val items: List<VoucherItemResponse>,
    val voucherCode: String? = null,
    val amount: Int,
    val discount: Int,
    val total: Int,
    val paymentMethod: String,
    val currency: String,
    val status: String,
    val createdAt: String,
    val voucherMetadata: VoucherMetadataResponse? = null
)

@Serializable
data class VoucherItemResponse(
    val id: String,
    val type: String,
    val qty: Int,
    val metadata: VoucherItemMetadataResponse
)

@Serializable
data class VoucherItemMetadataResponse(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val value: VoucherValueResponse,
    val conditions: VoucherConditionsResponse,
    val imageUri: String
)

@Serializable
data class VoucherValueResponse(
    val type: String,
    val amount: Int
)

@Serializable
data class VoucherConditionsResponse(
    val maxClaim: Int,
    val maxDiscountAmount: Int
)

@Serializable
data class VoucherMetadataResponse(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val value: VoucherValueResponse,
    val conditions: VoucherConditionsResponse,
    val imageUri: String
)

@Serializable
data class PaginationResponse(
    val limit: Int,
    val sortBy: String,
    val sortOrder: String,
    val lastDocId: String? = null,
    val firstDocId: String? = null,
    val direction: String,
    val hasMoreNext: Boolean,
    val hasMorePrev: Boolean
)
