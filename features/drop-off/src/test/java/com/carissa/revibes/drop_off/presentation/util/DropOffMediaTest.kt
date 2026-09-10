package com.carissa.revibes.drop_off.presentation.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class DropOffMediaTest {

    @Test
    fun `blank pcs count is rejected`() {
        assertNull(parsePositiveCount(""))
    }

    @Test
    fun `zero pcs is rejected`() {
        assertNull(parsePositiveCount("0"))
    }

    @Test
    fun `exact bottle count is kept`() {
        assertEquals(8, parsePositiveCount("8"))
    }

    @Test
    fun `missing picker type falls back to jpeg`() {
        assertEquals("image/jpeg", normalizedContentType(null))
    }

    @Test
    fun `charset suffix is stripped from content type`() {
        assertEquals("image/heic", normalizedContentType("image/heic; charset=utf-8"))
    }

    @Test
    fun `blank content type falls back to jpeg`() {
        assertEquals("image/jpeg", normalizedContentType("   "))
    }
}
