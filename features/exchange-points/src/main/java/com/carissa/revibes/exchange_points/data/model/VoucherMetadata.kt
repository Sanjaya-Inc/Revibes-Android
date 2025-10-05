package com.carissa.revibes.exchange_points.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoucherMetadata(
    @SerialName("id")
    val id: String,
    @SerialName("code")
    val code: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("imageUri")
    val imageUri: String,
    @SerialName("guides")
    val guides: List<String> = emptyList(),
    @SerialName("termConditions")
    val termConditions: List<String> = emptyList()
)
