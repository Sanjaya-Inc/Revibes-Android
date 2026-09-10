package com.carissa.revibes.drop_off.presentation.util

internal fun parsePositiveCount(raw: String): Int? {
    return raw.toIntOrNull()?.takeIf { it > 0 }
}

internal fun normalizedContentType(raw: String?): String {
    return raw?.substringBefore(';')
        ?.trim()
        ?.lowercase()
        ?.ifBlank { null }
        ?: "image/jpeg"
}
