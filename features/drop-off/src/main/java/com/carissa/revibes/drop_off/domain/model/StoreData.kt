package com.carissa.revibes.drop_off.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreData(
    val id: String,
    val name: String,
    val country: String,
    val address: String,
    val postalCode: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double,
    val status: String
)
