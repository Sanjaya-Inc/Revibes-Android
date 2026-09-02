package com.carissa.revibes.home_admin.domain

import java.net.URI

object GoogleMapsPoint {
    fun searchUrl(latitude: Double, longitude: Double): String {
        return "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
    }

    fun isValid(latitude: Double, longitude: Double): Boolean {
        return latitude in MIN_LATITUDE..MAX_LATITUDE && longitude in MIN_LONGITUDE..MAX_LONGITUDE
    }

    fun looksLikeMapsShare(input: String): Boolean {
        val lower = input.lowercase()
        return MAPS_HINTS.any { it in lower } || parse(input) != null
    }

    fun isShortLink(input: String): Boolean {
        val url = extractUrl(input) ?: return false
        val uri = runCatching { URI(url) }.getOrNull() ?: return false
        val host = uri.host?.lowercase() ?: return false
        val path = uri.path.orEmpty()
        return when (host) {
            "maps.app.goo.gl" -> path.length >= MIN_SHORT_LINK_PATH
            "goo.gl" -> path.startsWith("/maps/") && path.length > GOO_GL_MAPS_PREFIX_LENGTH
            else -> false
        }
    }

    fun extractUrl(input: String): String? {
        return URL_REGEX.find(input)?.value?.trimEnd(*URL_TRAILING_CHARS)
    }

    fun extractPlaceName(input: String): String? {
        val firstLine = input.lineSequence().map { it.trim() }.firstOrNull { it.isNotBlank() }
            ?: return null
        if (firstLine.startsWith("http", ignoreCase = true) ||
            firstLine.startsWith("geo:", ignoreCase = true)
        ) {
            return null
        }
        return firstLine.take(MAX_PLACE_NAME_LENGTH)
    }

    fun parse(input: String): Pair<Double, Double>? {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return null
        val fromUrl = extractUrl(trimmed)?.let(::matchCoords)
        return fromUrl ?: matchCoords(trimmed)
    }

    private fun matchCoords(text: String): Pair<Double, Double>? {
        val match = COORD_REGEXES.firstNotNullOfOrNull { regex -> regex.find(text) }
        return match?.let { latLngOrNull(it.groupValues[1], it.groupValues[2]) }
    }

    private fun latLngOrNull(latText: String, lngText: String): Pair<Double, Double>? {
        val latitude = latText.toDoubleOrNull()
        val longitude = lngText.toDoubleOrNull()
        return if (latitude != null && longitude != null && isValid(latitude, longitude)) {
            latitude to longitude
        } else {
            null
        }
    }

    private val COORD_REGEXES = listOf(
        Regex("""(?:[?&](?:query|q)=)(-?\d+(?:\.\d+)?),(-?\d+(?:\.\d+)?)"""),
        Regex("""!3d(-?\d+(?:\.\d+)?)!4d(-?\d+(?:\.\d+)?)"""),
        Regex("""(?:[?&]ll=)(-?\d+(?:\.\d+)?),(-?\d+(?:\.\d+)?)"""),
        Regex("""@(-?\d+(?:\.\d+)?),(-?\d+(?:\.\d+)?)"""),
        Regex("""geo:(-?\d+(?:\.\d+)?),(-?\d+(?:\.\d+)?)"""),
        Regex("""^(-?\d+(?:\.\d+)?)\s*,\s*(-?\d+(?:\.\d+)?)$""")
    )

    private val URL_REGEX = Regex("""https?://[^\s]+""", RegexOption.IGNORE_CASE)
    private val URL_TRAILING_CHARS = charArrayOf('.', ',', ')', ']', '"')
    private val MAPS_HINTS = listOf(
        "maps.app.goo.gl",
        "goo.gl/maps",
        "maps.google.",
        "google.com/maps",
        "google.co.id/maps",
        "geo:"
    )

    private const val MIN_LATITUDE = -90.0
    private const val MAX_LATITUDE = 90.0
    private const val MIN_LONGITUDE = -180.0
    private const val MAX_LONGITUDE = 180.0
    private const val MIN_SHORT_LINK_PATH = 5
    private const val GOO_GL_MAPS_PREFIX_LENGTH = 6
    private const val MAX_PLACE_NAME_LENGTH = 80
}
