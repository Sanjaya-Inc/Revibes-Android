package com.carissa.revibes.exchange_points.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeItem(
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: String,
    @SerialName("prices")
    val prices: List<Price>,
    @SerialName("quota")
    val quota: Int,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("metadata")
    val metadata: VoucherMetadata
)
