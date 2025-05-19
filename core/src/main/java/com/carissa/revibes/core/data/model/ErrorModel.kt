package com.carissa.revibes.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorModel(
    val status: String,
    val error: String,
    val reasons: List<String>
)
