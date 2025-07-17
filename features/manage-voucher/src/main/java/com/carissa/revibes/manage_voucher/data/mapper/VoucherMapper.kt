package com.carissa.revibes.manage_voucher.data.mapper

import com.carissa.revibes.manage_voucher.data.model.VoucherConditionsData
import com.carissa.revibes.manage_voucher.data.model.VoucherData
import com.carissa.revibes.manage_voucher.domain.model.VoucherConditions
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

fun VoucherData.toVoucherDomain(): VoucherDomain {
    return VoucherDomain(
        id = id,
        code = code,
        name = name,
        description = description,
        type = when (type.lowercase()) {
            "percent-off" -> VoucherDomain.VoucherType.PERCENT_OFF
            "fixed-amount" -> VoucherDomain.VoucherType.FIXED_AMOUNT
            else -> VoucherDomain.VoucherType.PERCENT_OFF
        },
        amount = amount,
        currency = when (currency.uppercase()) {
            "IDR" -> VoucherDomain.Currency.IDR
            "USD" -> VoucherDomain.Currency.USD
            "EUR" -> VoucherDomain.Currency.EUR
            "GBP" -> VoucherDomain.Currency.GBP
            "JPY" -> VoucherDomain.Currency.JPY
            else -> VoucherDomain.Currency.IDR
        },
        conditions = conditions.toVoucherConditions(),
        claimPeriodStart = claimPeriodStart,
        claimPeriodEnd = claimPeriodEnd,
        imageUrl = imageUrl,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun VoucherConditionsData.toVoucherConditions(): VoucherConditions {
    return VoucherConditions(
        maxClaim = maxClaim,
        maxUsage = maxUsage,
        minOrderItem = minOrderItem,
        minOrderAmount = minOrderAmount,
        maxDiscountAmount = maxDiscountAmount
    )
}

fun List<VoucherData>.toVoucherDomainList(): PersistentList<VoucherDomain> {
    return map { it.toVoucherDomain() }.toPersistentList()
}

fun VoucherDomain.VoucherType.toApiString(): String {
    return when (this) {
        VoucherDomain.VoucherType.PERCENT_OFF -> "percent-off"
        VoucherDomain.VoucherType.FIXED_AMOUNT -> "fixed-amount"
    }
}

fun VoucherDomain.Currency.toApiString(): String {
    return when (this) {
        VoucherDomain.Currency.IDR -> "idr"
        VoucherDomain.Currency.USD -> "usd"
        VoucherDomain.Currency.EUR -> "eur"
        VoucherDomain.Currency.GBP -> "gbp"
        VoucherDomain.Currency.JPY -> "jpy"
    }
}
