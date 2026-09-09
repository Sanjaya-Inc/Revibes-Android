package com.carissa.revibes.point.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PointHistoryTest {

    @Test
    fun `daily-reward history is a check-in`() {
        val sut = history(sourceType = "daily-reward")

        assertEquals(PointSourceKind.DAILY_CHECK_IN, sut.sourceKind())
    }

    @Test
    fun `logistic-order history is a drop-off`() {
        val sut = history(sourceType = "logistic-order")

        assertEquals(PointSourceKind.DROP_OFF, sut.sourceKind())
    }

    @Test
    fun `mission history is a mission`() {
        val sut = history(sourceType = "mission")

        assertEquals(PointSourceKind.MISSION, sut.sourceKind())
    }

    @Test
    fun `exchange history is an exchange`() {
        val sut = history(sourceType = "exchange")

        assertEquals(PointSourceKind.EXCHANGE, sut.sourceKind())
    }

    @Test
    fun `unknown source type stays unknown`() {
        val sut = history(sourceType = "referral")

        assertEquals(PointSourceKind.UNKNOWN, sut.sourceKind())
    }

    @Test
    fun `minus symbol prefixes a negative amount`() {
        val sut = history(symbol = "minus", value = 20)

        assertEquals("-20", sut.signedAmount())
    }

    @Test
    fun `plus symbol prefixes a positive amount`() {
        val sut = history(symbol = "plus", value = 5)

        assertEquals("+5", sut.signedAmount())
    }

    private fun history(
        sourceType: String? = "daily-reward",
        symbol: String = "plus",
        value: Int = 5
    ) = PointHistory(
        id = "hist-1",
        timestamp = "2026-09-09T00:00:00Z",
        sourceType = sourceType,
        symbol = symbol,
        value = value
    )
}
