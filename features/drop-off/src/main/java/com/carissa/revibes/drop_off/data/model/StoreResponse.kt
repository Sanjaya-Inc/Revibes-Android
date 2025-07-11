package com.carissa.revibes.drop_off.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class StoreResponse(
    val code: Int,
    val message: String,
    val data: List<Store>,
    val status: String
)

@Keep
@Serializable
data class Store(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val country: String,
    val address: String,
    val postalCode: String,
    val position: StorePosition,
    val status: String
)

@Keep
@Serializable
data class StorePosition(
    val latitude: Double,
    val longitude: Double,
    val distance: Double
)
