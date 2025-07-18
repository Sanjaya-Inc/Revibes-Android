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
    val type: VoucherType,
    val amount: Double,
//    val currency: Currency,
    val conditions: VoucherConditions?,
    val claimPeriodStart: String,
    val claimPeriodEnd: String,
    val imageUrl: String? = null,
    val isActive: Boolean = true,
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

//    enum class Currency {
//        IDR, USD, EUR, GBP, JPY
//    }
}

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
