package com.carissa.revibes.exchange_points.data.mapper

import com.carissa.revibes.exchange_points.data.model.ExchangeItem
import com.carissa.revibes.exchange_points.data.model.UserVoucherItem
import com.carissa.revibes.exchange_points.domain.model.Voucher
import com.carissa.revibes.exchange_points.domain.model.UserVoucher

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

fun UserVoucherItem.toUserVoucher(): UserVoucher {
    return UserVoucher(
        id = this.id,
        voucherId = this.voucherId,
        status = this.status,
        claimedAt = this.claimedAt,
        expiredAt = this.expiredAt,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.metadata.name,
        description = this.metadata.description,
        imageUri = this.metadata.imageUri.ifNotUrlOrBlank("https://placehold.co/1200x800.png"),
        code = this.metadata.code,
        guides = this.metadata.guides,
        termConditions = this.metadata.termConditions
    )
}

private fun String.ifNotUrlOrBlank(default: String): String {
    return if (this.isBlank() || !this.contains("http")) default else this
}
