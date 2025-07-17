package com.carissa.revibes.manage_voucher.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class CreateVoucherRequest(
    val code: String,
    val name: String,
    val description: String,
    val type: String,
    val amount: Double,
    val currency: String,
    val conditions: VoucherConditionsRequest,
    val claimPeriodStart: String,
    val claimPeriodEnd: String,
    val imageUrl: String? = null
)

@Serializable
@Keep
data class UpdateVoucherRequest(
    val code: String,
    val name: String,
    val description: String,
    val type: String,
    val amount: Double,
    val currency: String,
    val conditions: VoucherConditionsRequest,
    val claimPeriodStart: String,
    val claimPeriodEnd: String,
    val imageUrl: String? = null,
    val isActive: Boolean
)

@Serializable
@Keep
data class VoucherConditionsRequest(
    val maxClaim: Int,
    val maxUsage: Int,
    val minOrderItem: Int,
    val minOrderAmount: Double,
    val maxDiscountAmount: Double
)
