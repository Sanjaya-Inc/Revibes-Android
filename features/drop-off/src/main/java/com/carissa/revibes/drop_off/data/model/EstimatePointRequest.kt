package com.carissa.revibes.drop_off.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EstimatePointRequest(
    val items: List<EstimatePointItem>
)

@Serializable
data class EstimatePointItem(
    val name: String,
    val type: String,
    val weight: Int
)
