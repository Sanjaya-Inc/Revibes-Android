package com.carissa.revibes.manage_transaction.data.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AppSettingPointDataTest {

    private val sut = AppSettingPointData(organic = 3, nonOrganic = 5, b3 = 8)

    @Test
    fun `organic type uses organic rate`() {
        assertEquals(3, sut.forType("organic"))
    }

    @Test
    fun `non organic type uses non organic rate`() {
        assertEquals(5, sut.forType("non-organic"))
    }

    @Test
    fun `b3 type uses b3 rate`() {
        assertEquals(8, sut.forType("b3"))
    }

    @Test
    fun `unknown type uses five`() {
        assertEquals(5, sut.forType("plastic"))
    }
}
