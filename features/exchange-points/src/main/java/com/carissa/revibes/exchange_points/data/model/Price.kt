package com.carissa.revibes.exchange_points.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Price(
    @SerialName("amount")
    val amount: Int,
    @SerialName("currency")
    val currency: String
)
