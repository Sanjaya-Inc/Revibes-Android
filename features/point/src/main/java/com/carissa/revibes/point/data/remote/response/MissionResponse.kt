package com.carissa.revibes.point.data.remote.response

import com.carissa.revibes.core.data.user.model.Pagination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MissionResponse(

    @SerialName("data")
    val data: MissionDataResponse,

    @SerialName("message")
    val message: String,

    @SerialName("status")
    val status: String
)

@Serializable
data class MissionDataResponse(
    @SerialName("items")
    val items: List<MissionItemResponse>,
    @SerialName("pagination")
    val pagination: Pagination
)

@Serializable
data class MissionItemResponse(
    val id: String,
    val type: String,
    val mission: MissionItemDetailResponse,
)

@Serializable
data class MissionItemDetailResponse(
    val imageUri: String? = null,
    val reward: Int,
    val description: String,
    val id: String,
    val title: String,
    val subtitle: String,
)
