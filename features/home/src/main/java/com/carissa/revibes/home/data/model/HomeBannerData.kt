package com.carissa.revibes.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeBannerData(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("uri")
    val imageUrl: String,
    val deeplink: String = ""
)

@Serializable
data class BannerResponse(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<HomeBannerData>,
    @SerialName("status")
    val status: String
)
