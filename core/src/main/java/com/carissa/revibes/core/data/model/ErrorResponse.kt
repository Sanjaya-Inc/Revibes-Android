package com.carissa.revibes.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: String = "",
    val code: Int = 0,
    val error: String = "",
    val message: String = ""
) {
    val displayMessage: String
        get() = when {
            message.isNotBlank() -> message
            error.isNotBlank() -> humanizeErrorCode(error)
            status.isNotBlank() -> status
            else -> ""
        }

    private fun humanizeErrorCode(code: String): String {
        if (code.contains('.') || code.contains('_')) {
            return code.split('.', '_')
                .filter { it.isNotBlank() }
                .joinToString(" ") { word ->
                    word.lowercase().replaceFirstChar { it.uppercase() }
                }
        }
        return code
    }
}
