package com.carissa.revibes.manage_voucher.data.mapper

import com.carissa.revibes.manage_voucher.data.model.VoucherConditionsData
import com.carissa.revibes.manage_voucher.data.model.VoucherData
import com.carissa.revibes.manage_voucher.data.model.VoucherItemData
import com.carissa.revibes.manage_voucher.domain.model.VoucherConditions
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain

fun VoucherData.toDomain(): VoucherDomain {
    return VoucherDomain(
        id = this.id,
        code = this.code,
        name = this.name,
        description = this.description,
        type = this.type.toVoucherType(),
        amount = this.amount,
//        currency = this.currency.toCurrency(),
        conditions = this.conditions.toDomain(),
        claimPeriodStart = this.claimPeriodStart,
        claimPeriodEnd = this.claimPeriodEnd,
        imageUrl = this.imageUrl,
        isActive = true,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun VoucherConditionsData.toDomain(): VoucherConditions {
    return VoucherConditions(
        maxClaim = this.maxClaim,
        maxUsage = this.maxUsage,
        minOrderItem = this.minOrderItem,
        minOrderAmount = this.minOrderAmount,
        maxDiscountAmount = this.maxDiscountAmount
    )
}

fun String.toVoucherType(): VoucherDomain.VoucherType {
    return when (this) {
        "percent-off" -> VoucherDomain.VoucherType.PERCENT_OFF
        "fixed-amount" -> VoucherDomain.VoucherType.FIXED_AMOUNT
        else -> throw IllegalArgumentException("Unknown voucher type: $this")
    }
}

fun VoucherItemData.toDomain(): VoucherDomain {
    return VoucherDomain(
        id = this.id,
        code = this.code,
        name = this.name,
        description = "",
        type = this.value.type.toVoucherType(),
        amount = this.value.amount.toDouble(),
//        currency = this.currency.toCurrency(),
        conditions = null,
        claimPeriodStart = this.claimPeriodStart,
        claimPeriodEnd = this.claimPeriodEnd,
        imageUrl = this.imageUri,
        isActive = true,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

// fun String.toCurrency(): VoucherDomain.Currency {
//    return when (this) {
//        "IDR" -> VoucherDomain.Currency.IDR
//        "USD" -> VoucherDomain.Currency.USD
//        "EUR" -> VoucherDomain.Currency.EUR
//        "GBP" -> VoucherDomain.Currency.GBP
//        "JPY" -> VoucherDomain.Currency.JPY
//        else -> throw IllegalArgumentException("Unknown currency: $this")
//    }
// }
