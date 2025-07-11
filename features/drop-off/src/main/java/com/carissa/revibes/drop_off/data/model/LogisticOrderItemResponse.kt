package com.carissa.revibes.drop_off.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LogisticOrderItemResponse(
    val code: Int,
    val message: String,
    val data: String, // Item ID
    val status: String
)
