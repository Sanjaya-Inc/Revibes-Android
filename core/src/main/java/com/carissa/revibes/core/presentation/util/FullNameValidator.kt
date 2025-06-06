package com.carissa.revibes.core.presentation.util

object FullNameValidator {
    fun validate(fullName: String): String? {
        return when {
            fullName.isBlank() -> ""
            fullName.length < 4 -> "Full name is too short"
            else -> null
        }
    }
}
