package com.carissa.revibes.manage_voucher.data.mapper

import com.carissa.revibes.manage_voucher.data.model.ExchangeVoucherData
import com.carissa.revibes.manage_voucher.domain.model.ExchangeVoucherDomain
import com.carissa.revibes.manage_voucher.domain.model.ExchangePriceDomain

fun ExchangeVoucherData.toDomain(): ExchangeVoucherDomain {
    return ExchangeVoucherDomain(
        id = this.id,
        type = this.type,
        sourceId = this.sourceId,
        description = this.description,
        prices = this.prices.map {
            ExchangePriceDomain(
                amount = it.amount,
                currency = it.currency
            )
        },
        quota = this.quota,
        availableAt = this.availableAt,
        endedAt = this.endedAt,
        isAvailable = this.isAvailable,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
