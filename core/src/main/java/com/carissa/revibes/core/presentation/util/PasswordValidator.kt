package com.carissa.revibes.core.presentation.util

object PasswordValidator {
    private const val ERROR_EMPTY = ""
    private const val ERROR_LENGTH = "Password must be at least 8 characters long."
    private const val ERROR_DIGIT = "Password must contain at least one digit."
    private const val ERROR_UPPERCASE = "Password must contain at least one uppercase letter."
    private const val ERROR_LOWERCASE = "Password must contain at least one lowercase letter."
    private const val ERROR_SPECIAL_CHAR = "Password must contain at least one special character."
    private const val ERROR_MISMATCH = "Passwords do not match."

    fun validate(password: String): String? {
        return when {
            password.isEmpty() -> ERROR_EMPTY
            password.length < 8 -> ERROR_LENGTH
            !password.any { it.isDigit() } -> ERROR_DIGIT
            !password.any { it.isUpperCase() } -> ERROR_UPPERCASE
            !password.any { it.isLowerCase() } -> ERROR_LOWERCASE
            !password.any { !it.isLetterOrDigit() } -> ERROR_SPECIAL_CHAR
            else -> null
        }
    }

    fun validate(password: String, passwordConfirm: String): String? {
        val basicValidation = validate(password)
        if (basicValidation != null) return basicValidation
        return if (password != passwordConfirm) ERROR_MISMATCH else null
    }
}
