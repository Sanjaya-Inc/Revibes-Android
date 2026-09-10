package com.carissa.revibes.home_admin.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class DropOffPointValuesTest {

    @Test
    fun `blank organic value is rejected`() {
        assertNull(parseDropOffPointValues("", "5", "5"))
    }

    @Test
    fun `non numeric value is rejected`() {
        assertNull(parseDropOffPointValues("5", "abc", "5"))
    }

    @Test
    fun `zero is a valid point value`() {
        assertEquals(
            DropOffPointValues(organic = 0, nonOrganic = 5, b3 = 8),
            parseDropOffPointValues("0", "5", "8")
        )
    }

    @Test
    fun `typed conversion rates are kept`() {
        assertEquals(
            DropOffPointValues(organic = 3, nonOrganic = 12, b3 = 8),
            parseDropOffPointValues("3", "12", "8")
        )
    }
}
