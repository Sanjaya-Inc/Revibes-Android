package com.carissa.revibes.exchange_points.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeDataResponse(
    val code: Int,
    val message: String,
    val status: String,
    val data: ExchangeData,
)

@Serializable
data class ExchangeData(
    @SerialName("items")
    val items: List<ExchangeItem>,
    @SerialName("pagination")
    val pagination: Pagination
)
