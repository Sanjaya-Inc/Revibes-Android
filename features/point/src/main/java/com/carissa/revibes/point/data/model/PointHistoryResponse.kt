package com.carissa.revibes.point.data.model

import com.carissa.revibes.core.data.user.model.Pagination
import kotlinx.serialization.Serializable

@Serializable
data class PointHistoryResponse(
    val status: String,
    val message: String,
    val data: PointHistoryDataResponse
)

@Serializable
data class PointHistoryDataResponse(
    val items: List<PointHistoryItemData>,
    val pagination: Pagination
)

@Serializable
data class PointHistoryItemData(
    val id: String,
    val timestamp: String,
    val sourceType: String? = null,
    val sourceId: String? = null,
    val symbol: String,
    val value: Int,
    val prevValue: Int,
    val newValue: Int
)
