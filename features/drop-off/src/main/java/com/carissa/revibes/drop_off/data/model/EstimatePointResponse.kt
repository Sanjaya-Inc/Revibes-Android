package com.carissa.revibes.drop_off.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EstimatePointResponse(
    val code: Int,
    val message: String,
    val data: EstimatePointData,
    val status: String
)

@Serializable
data class EstimatePointData(
    val items: Map<String, Int>,
    val total: Int
)
