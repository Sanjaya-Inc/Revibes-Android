package com.carissa.revibes.drop_off.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SubmitOrderResponse(
    val code: Int,
    val message: String,
    val status: String
)

@Keep
@Serializable
data class SubmitOrderRequest(
    val type: String,
    val name: String,
    val country: String,
    @SerialName("storeLocation")
    val storeId: String,
    val items: List<SubmitOrderItem>
)

@Keep
@Serializable
data class SubmitOrderItem(
    val id: String,
    val name: String,
    val type: String,
    val weight: Int
)
