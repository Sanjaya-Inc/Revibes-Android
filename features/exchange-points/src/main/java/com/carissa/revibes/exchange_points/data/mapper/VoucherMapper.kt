package com.carissa.revibes.exchange_points.data.mapper

import com.carissa.revibes.exchange_points.data.model.ExchangeItem
import com.carissa.revibes.exchange_points.domain.model.Voucher

fun ExchangeItem.toVoucher(): Voucher {
    return Voucher(
        id = this.id,
        name = this.metadata.name,
        description = this.metadata.description,
        imageUri = this.metadata.imageUri.ifNotUrlOrBlank("https://placehold.co/1200x800.png"),
        point = this.prices.first().amount,
        quota = this.quota
    )
}

private fun String.ifNotUrlOrBlank(default: String): String {
    return if (this.isBlank() || !this.contains("http")) default else this
}
