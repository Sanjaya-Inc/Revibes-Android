package com.carissa.revibes.point.data.mapper

import com.carissa.revibes.point.data.model.DailyRewardData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DailyRewardMapperTest {

    @Test
    fun `daily reward preserves backend amount instead of hardcoding 1`() {
        val data = DailyRewardData(
            id = "reward-1",
            index = 3,
            amount = 7,
            createdAt = "2026-09-26T00:00:00Z",
            claimedAt = null,
            bannerText = "Custom banner text from admin"
        )

        val result = data.toDailyPoint()

        assertEquals(7, result.amount)
        assertEquals(3, result.dayIndex)
        assertEquals("Custom banner text from admin", result.bannerText)
    }
}
