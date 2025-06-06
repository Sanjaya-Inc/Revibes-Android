package com.carissa.revibes.core.presentation.util

object EmailValidator {
    fun validate(email: String): String? {
        return when {
            email.isEmpty() -> ""
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email"
            else -> null
        }
    }
}
