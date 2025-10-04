package com.carissa.revibes.manage_voucher.domain.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Stable
data class VoucherDomain(
    val id: String,
    val code: String,
    val name: String,
    val description: String,
    val value: VoucherValue,
    val conditions: VoucherConditions?,
    val imageUri: String? = null,
    val claimPeriodStart: String,
    val claimPeriodEnd: String,
    val isAvailable: Boolean = true,
    val inUse: Boolean = false,
    val createdAt: String,
    val updatedAt: String
) {
    enum class VoucherType(val value: String) {
        PERCENT_OFF("percent-off"),
        FIXED_AMOUNT("fixed-amount");

        override fun toString(): String {
            return value
        }
    }
}

@Serializable
@Keep
@Stable
data class VoucherValue(
    val type: VoucherDomain.VoucherType,
    val amount: Double
)

@Serializable
@Keep
@Stable
data class VoucherConditions(
    val maxClaim: Int,
    val maxUsage: Int,
    val minOrderItem: Int,
    val minOrderAmount: Long,
    val maxDiscountAmount: Long,
)
