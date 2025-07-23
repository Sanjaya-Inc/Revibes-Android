package com.carissa.revibes.exchange_points.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseItem(
    @SerialName("id")
    val id: String,

    @SerialName("qty")
    val qty: Int
)
