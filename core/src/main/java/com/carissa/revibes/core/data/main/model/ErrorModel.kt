package com.carissa.revibes.core.data.main.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorModel(
    val status: String = "",
    val error: String = "",
    val message: String = "",
    val reasons: List<String> = emptyList()
) {
    val displayMessage: String
        get() = when {
            message.isNotBlank() -> message
            reasons.any { it.isNotBlank() } -> reasons.first { it.isNotBlank() }
            error.isNotBlank() -> humanizeErrorCode(error)
            else -> "An error occurred"
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
