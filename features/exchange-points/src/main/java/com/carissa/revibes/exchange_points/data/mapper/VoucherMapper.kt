package com.carissa.revibes.exchange_points.data.mapper

import com.carissa.revibes.exchange_points.data.model.ExchangeItem
import com.carissa.revibes.exchange_points.domain.model.Voucher

fun ExchangeItem.toVoucher(): Voucher {
    return Voucher(
        id = this.id,
        name = this.metadata.name,
        description = this.metadata.description,
        imageUri = this.metadata.imageUri.run {
            if (this.startsWith("http")) {
                this
            } else {
                "https://alkuwaiti.com/wp-content/uploads/2020/05/Hero-Banner-Placeholder-Dark-1024x480.png"
            }
        },
        point = this.prices.first().amount,
        quota = this.quota
    )
}
