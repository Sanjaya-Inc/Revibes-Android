package com.carissa.revibes.claimed_vouchers.domain.model

data class ClaimedVoucherDomain(
    val id: String,
    val items: List<ClaimedVoucherItemDomain>,
    val voucherCode: String?,
    val amount: Int,
    val discount: Int,
    val total: Int,
    val paymentMethod: String,
    val currency: String,
    val status: String,
    val createdAt: String,
    val voucherMetadata: VoucherMetadataDomain?
)

data class ClaimedVoucherItemDomain(
    val id: String,
    val type: String,
    val qty: Int,
    val metadata: VoucherItemMetadataDomain
)

data class VoucherItemMetadataDomain(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val value: VoucherValueDomain,
    val conditions: VoucherConditionsDomain,
    val imageUri: String
)

data class VoucherValueDomain(
    val type: String,
    val amount: Int
)

data class VoucherConditionsDomain(
    val maxClaim: Int,
    val maxDiscountAmount: Int
)

data class VoucherMetadataDomain(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val value: VoucherValueDomain,
    val conditions: VoucherConditionsDomain,
    val imageUri: String
)
