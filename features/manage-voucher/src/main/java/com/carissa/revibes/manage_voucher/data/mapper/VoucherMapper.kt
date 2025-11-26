package com.carissa.revibes.manage_voucher.data.mapper

import com.carissa.revibes.manage_voucher.data.model.VoucherConditionsData
import com.carissa.revibes.manage_voucher.data.model.VoucherData
import com.carissa.revibes.manage_voucher.data.model.VoucherItemData
import com.carissa.revibes.manage_voucher.domain.model.VoucherConditions
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import com.carissa.revibes.manage_voucher.domain.model.VoucherValue

fun VoucherData.toDomain(): VoucherDomain {
    return VoucherDomain(
        id = this.id,
        code = this.code,
        name = this.name,
        description = this.description,
        value = VoucherValue(
            type = this.value.type.toVoucherType(),
            amount = this.value.amount
        ),
        conditions = this.conditions.toDomain(),
        imageUri = this.imageUri,
        claimPeriodStart = this.claimPeriodStart,
        claimPeriodEnd = this.claimPeriodEnd,
        isAvailable = this.isAvailable,
        inUse = this.inUse,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        termConditions = this.termConditions,
        guides = this.guides
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
        description = this.description,
        value = VoucherValue(
            type = this.value.type.toVoucherType(),
            amount = this.value.amount
        ),
        conditions = this.conditions.toDomain(),
        imageUri = this.imageUri,
        claimPeriodStart = this.claimPeriodStart,
        claimPeriodEnd = this.claimPeriodEnd,
        isAvailable = this.isAvailable,
        inUse = this.inUse,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        termConditions = this.termConditions,
        guides = this.guides
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
