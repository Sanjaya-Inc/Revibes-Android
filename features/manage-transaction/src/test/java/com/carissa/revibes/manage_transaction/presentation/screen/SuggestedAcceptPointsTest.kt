package com.carissa.revibes.manage_transaction.presentation.screen

import com.carissa.revibes.manage_transaction.data.model.AppSettingPointData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SuggestedAcceptPointsTest {

    private val rates = AppSettingPointData(organic = 3, nonOrganic = 5, b3 = 8)

    @Test
    fun `uses stored total when already set`() {
        assertEquals(
            20,
            suggestedAcceptPoints(
                totalPoint = 20,
                itemPoints = listOf(0),
                itemTypes = listOf("non-organic"),
                rates = rates
            )
        )
    }

    @Test
    fun `sums item points when total is still zero`() {
        assertEquals(
            9,
            suggestedAcceptPoints(
                totalPoint = 0,
                itemPoints = listOf(4, 5),
                itemTypes = listOf("organic", "non-organic"),
                rates = rates
            )
        )
    }

    @Test
    fun `falls back to type rates for pending drop-off`() {
        assertEquals(
            5,
            suggestedAcceptPoints(
                totalPoint = 0,
                itemPoints = listOf(0),
                itemTypes = listOf("non-organic"),
                rates = rates
            )
        )
    }

    @Test
    fun `sums type rates across items`() {
        assertEquals(
            11,
            suggestedAcceptPoints(
                totalPoint = 0,
                itemPoints = listOf(0, 0),
                itemTypes = listOf("organic", "b3"),
                rates = rates
            )
        )
    }

    @Test
    fun `empty order suggests zero`() {
        assertEquals(
            0,
            suggestedAcceptPoints(
                totalPoint = 0,
                itemPoints = emptyList(),
                itemTypes = emptyList(),
                rates = rates
            )
        )
    }
}
