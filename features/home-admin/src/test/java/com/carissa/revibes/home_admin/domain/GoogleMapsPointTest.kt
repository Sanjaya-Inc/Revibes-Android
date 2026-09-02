package com.carissa.revibes.home_admin.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class GoogleMapsPointTest {

    @Test
    fun `parse extracts coordinates from google maps search url`() {
        val result = GoogleMapsPoint.parse(
            "https://www.google.com/maps/search/?api=1&query=-6.3015,106.6527"
        )
        assertEquals(-6.3015 to 106.6527, result)
    }

    @Test
    fun `parse extracts coordinates from maps at pin`() {
        val result = GoogleMapsPoint.parse("https://www.google.com/maps/@-6.2,106.8,15z")
        assertEquals(-6.2 to 106.8, result)
    }

    @Test
    fun `parse extracts coordinates from geo uri`() {
        val result = GoogleMapsPoint.parse("geo:-6.1754,106.8272?q=-6.1754,106.8272(Monas)")
        assertEquals(-6.1754 to 106.8272, result)
    }

    @Test
    fun `parse extracts raw lat lng pair`() {
        val result = GoogleMapsPoint.parse("-6.3015, 106.6527")
        assertEquals(-6.3015 to 106.6527, result)
    }

    @Test
    fun `parse rejects invalid coordinates`() {
        assertNull(GoogleMapsPoint.parse("not a map"))
        assertNull(GoogleMapsPoint.parse("91.0,10.0"))
    }

    @Test
    fun `searchUrl uses google maps query deeplink`() {
        assertEquals(
            "https://www.google.com/maps/search/?api=1&query=-6.3015,106.6527",
            GoogleMapsPoint.searchUrl(-6.3015, 106.6527)
        )
    }

    @Test
    fun `parse extracts pin from place url data over camera at`() {
        val result = GoogleMapsPoint.parse(
            "https://www.google.com/maps/place/Monas/@-6.2,106.8,17z/data=!3d-6.1753924!4d106.8271528"
        )
        assertEquals(-6.1753924 to 106.8271528, result)
    }

    @Test
    fun `parse extracts coordinates from ll query`() {
        val result = GoogleMapsPoint.parse("https://maps.google.com/?ll=-6.3015,106.6527")
        assertEquals(-6.3015 to 106.6527, result)
    }

    @Test
    fun `parse extracts coordinates from share text with later url`() {
        val result = GoogleMapsPoint.parse(
            """
            Monas
            https://www.google.com/maps/search/?api=1&query=-6.1754,106.8272
            """.trimIndent()
        )
        assertEquals(-6.1754 to 106.8272, result)
    }

    @Test
    fun `extractPlaceName uses first non url line`() {
        assertEquals(
            "Monas",
            GoogleMapsPoint.extractPlaceName(
                "Monas\nhttps://maps.app.goo.gl/abcdef"
            )
        )
        assertNull(GoogleMapsPoint.extractPlaceName("https://maps.app.goo.gl/abcdef"))
    }

    @Test
    fun `looksLikeMapsShare accepts google maps hosts`() {
        assertEquals(true, GoogleMapsPoint.looksLikeMapsShare("https://maps.app.goo.gl/abcdef"))
        assertEquals(false, GoogleMapsPoint.looksLikeMapsShare("just some text"))
    }

    @Test
    fun `isShortLink requires maps short host and path`() {
        assertEquals(true, GoogleMapsPoint.isShortLink("https://maps.app.goo.gl/abcdef"))
        assertEquals(true, GoogleMapsPoint.isShortLink("https://goo.gl/maps/abcdef"))
        assertEquals(false, GoogleMapsPoint.isShortLink("https://www.google.com/maps/@-6.2,106.8,15z"))
        assertEquals(false, GoogleMapsPoint.isShortLink("https://maps.app.goo.gl/"))
    }
}
