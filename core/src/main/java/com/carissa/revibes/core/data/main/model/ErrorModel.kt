package com.carissa.revibes.core.data.main.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorModel(
    val status: String,
    val error: String,
    val reasons: List<String>
)
