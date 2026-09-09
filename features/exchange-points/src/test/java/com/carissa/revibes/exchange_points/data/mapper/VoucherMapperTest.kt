package com.carissa.revibes.exchange_points.data.mapper

import com.carissa.revibes.exchange_points.data.model.ExchangeItem
import com.carissa.revibes.exchange_points.data.model.Price
import com.carissa.revibes.exchange_points.data.model.VoucherMetadata
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VoucherMapperTest {

    @Test
    fun `unavailable vouchers are omitted from the catalog`() {
        val items = listOf(
            exchangeItem(id = "voucher-on", isAvailable = true, amount = 50),
            exchangeItem(id = "voucher-off", isAvailable = false, amount = 80)
        )

        val result = items.toListedVouchers()

        assertEquals(listOf("voucher-on"), result.map { it.id })
        assertEquals(50, result.single().point)
    }

    private fun exchangeItem(
        id: String,
        isAvailable: Boolean,
        amount: Int
    ) = ExchangeItem(
        id = id,
        type = "voucher",
        prices = listOf(Price(amount = amount, currency = "point")),
        quota = 10,
        createdAt = "2026-09-01T00:00:00Z",
        updatedAt = "2026-09-01T00:00:00Z",
        isAvailable = isAvailable,
        metadata = VoucherMetadata(
            id = id,
            code = "CODE",
            name = "Coffee",
            description = "A free drink",
            imageUri = "https://example.com/voucher.png"
        )
    )
}
