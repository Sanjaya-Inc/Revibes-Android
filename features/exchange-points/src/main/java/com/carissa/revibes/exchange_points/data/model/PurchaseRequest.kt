package com.carissa.revibes.exchange_points.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequest(
    @SerialName("items")
    val items: List<PurchaseItem>,

    @SerialName("paymentMethod")
    val paymentMethod: String,

    @SerialName("currency")
    val currency: String
)
