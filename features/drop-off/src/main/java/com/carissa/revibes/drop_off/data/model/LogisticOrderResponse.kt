package com.carissa.revibes.drop_off.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LogisticOrderResponse(
    val code: Int,
    val message: String,
    val data: String, // Order ID
    val status: String
)
