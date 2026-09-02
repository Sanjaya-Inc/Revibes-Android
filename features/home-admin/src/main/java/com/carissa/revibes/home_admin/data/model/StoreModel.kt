package com.carissa.revibes.home_admin.data.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class StoreListResponse(
    val code: Int = 200,
    val message: String = "",
    val data: List<StoreData> = emptyList(),
    val status: String = ""
)

@Keep
@Serializable
data class StoreDetailResponse(
    val code: Int = 201,
    val message: String = "",
    val data: StoreData? = null,
    val status: String = ""
)

@Immutable
@Keep
@Serializable
data class StoreData(
    val id: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val name: String = "",
    val country: String = "",
    val address: String = "",
    val postalCode: String = "",
    val position: StorePositionData? = null,
    val status: String = "active"
)

@Immutable
@Keep
@Serializable
data class StorePositionData(
    val latitude: Double,
    val longitude: Double,
    val distance: Double? = null
)

@Keep
@Serializable
data class CreateStoreRequest(
    val name: String,
    val country: String,
    val address: String,
    val postalCode: String,
    val position: StorePositionRequest? = null
)

@Keep
@Serializable
data class StorePositionRequest(
    val latitude: Double,
    val longitude: Double
)

@Keep
@Serializable
data class UpdateStoreRequest(
    val name: String,
    val country: String,
    val address: String,
    val postalCode: String,
    val position: StorePositionRequest? = null,
    val status: String? = null
)
