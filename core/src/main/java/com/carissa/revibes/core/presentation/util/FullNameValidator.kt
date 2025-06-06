package com.carissa.revibes.core.presentation.util

object FullNameValidator {
    fun validate(fullName: String): String? {
        if (fullName.isBlank()) {
            return ""
        }
        return null
    }
}
