package com.carissa.revibes.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: String,
    val code: Int,
    val error: String
)
