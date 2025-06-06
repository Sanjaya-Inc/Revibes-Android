package com.carissa.revibes.core.presentation.util

object PhoneValidator {
    private const val INDONESIA_PHONE_REGEX = "^(\\+62|62|0)8[1-9][0-9]{6,9}$"

    fun validate(phone: String): String? {
        return when {
            phone.isBlank() -> ""
            !phone.matches(INDONESIA_PHONE_REGEX.toRegex()) -> "Invalid Indonesian phone number format"
            else -> null
        }
    }

    fun formatToInternational(phone: String): String {
        if (validate(phone) != null && !phone.startsWith("+62")) {
            // Attempt to normalize even if validation fails, but prioritize +62 if already there
            return when {
                phone.startsWith("62") -> "+$phone"
                phone.startsWith("0") -> "+62${phone.substring(1)}"
                else -> phone // Return as is if format is unknown or already international
            }
        } else if (phone.startsWith("+62")) {
            return phone
        }
        // Default to returning the original string if no clear transformation rule applies or already valid
        return phone
    }
}
