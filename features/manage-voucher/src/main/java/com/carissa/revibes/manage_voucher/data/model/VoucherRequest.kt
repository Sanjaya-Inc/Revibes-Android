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
    val imageUrl: String? = null,
    val termConditions: List<String> = emptyList(),
    val guides: List<String> = emptyList()
)

@Serializable
@Keep
data class UpdateVoucherRequest(
    val name: String,
    val description: String,
    val code: String? = null,
    val conditions: VoucherConditionsRequest? = null,
    val claimPeriodStart: String? = null,
    val claimPeriodEnd: String? = null,
    val termConditions: List<String>? = null,
    val guides: List<String>? = null
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
