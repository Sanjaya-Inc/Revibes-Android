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
    val currency: Currency,
    val conditions: VoucherConditions,
    val claimPeriodStart: String,
    val claimPeriodEnd: String,
    val imageUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: String,
    val updatedAt: String
) {
    enum class VoucherType {
        PERCENT_OFF, FIXED_AMOUNT
    }

    enum class Currency {
        IDR, USD, EUR, GBP, JPY
    }

    companion object {
        fun dummy() = listOf(
            VoucherDomain(
                id = "1",
                code = "SAVE20",
                name = "Save 20% Off",
                description = "Get 20% discount on your next purchase",
                type = VoucherType.PERCENT_OFF,
                amount = 20.0,
                currency = Currency.IDR,
                conditions = VoucherConditions(
                    maxClaim = 100,
                    maxUsage = 1,
                    minOrderItem = 1,
                    minOrderAmount = 50000.0,
                    maxDiscountAmount = 100000.0
                ),
                claimPeriodStart = "2024-01-01T00:00:00.000Z",
                claimPeriodEnd = "2024-12-31T23:59:59.000Z",
                imageUrl = null,
                isActive = true,
                createdAt = "2024-01-01T00:00:00.000Z",
                updatedAt = "2024-01-01T00:00:00.000Z"
            ),
            VoucherDomain(
                id = "2",
                code = "FIXED50K",
                name = "Fixed 50K Discount",
                description = "Get fixed 50,000 IDR discount",
                type = VoucherType.FIXED_AMOUNT,
                amount = 50000.0,
                currency = Currency.IDR,
                conditions = VoucherConditions(
                    maxClaim = 50,
                    maxUsage = 1,
                    minOrderItem = 2,
                    minOrderAmount = 100000.0,
                    maxDiscountAmount = 50000.0
                ),
                claimPeriodStart = "2024-02-01T00:00:00.000Z",
                claimPeriodEnd = "2024-11-30T23:59:59.000Z",
                imageUrl = null,
                isActive = true,
                createdAt = "2024-02-01T00:00:00.000Z",
                updatedAt = "2024-02-01T00:00:00.000Z"
            ),
            VoucherDomain(
                id = "3",
                code = "WELCOME15",
                name = "Welcome Bonus",
                description = "15% off for new customers",
                type = VoucherType.PERCENT_OFF,
                amount = 15.0,
                currency = Currency.IDR,
                conditions = VoucherConditions(
                    maxClaim = 200,
                    maxUsage = 1,
                    minOrderItem = 1,
                    minOrderAmount = 25000.0,
                    maxDiscountAmount = 75000.0
                ),
                claimPeriodStart = "2024-01-15T00:00:00.000Z",
                claimPeriodEnd = "2024-12-15T23:59:59.000Z",
                imageUrl = null,
                isActive = false,
                createdAt = "2024-01-15T00:00:00.000Z",
                updatedAt = "2024-01-15T00:00:00.000Z"
            )
        )
    }
}

@Serializable
@Keep
@Stable
data class VoucherConditions(
    val maxClaim: Int,
    val maxUsage: Int,
    val minOrderItem: Int,
    val minOrderAmount: Double,
    val maxDiscountAmount: Double
)
