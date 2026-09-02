package com.carissa.revibes.home_admin.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class NewsData(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: String? = null,
    val isActive: Boolean = true
)

@Keep
@Serializable
data class NewsResponse(
    val code: Int = 200,
    val message: String = "",
    val data: NewsData? = null,
    val status: String = ""
)

@Keep
@Serializable
data class CreateNewsRequest(
    val title: String,
    val content: String
)

@Keep
@Serializable
data class UpdateNewsRequest(
    val title: String,
    val content: String
)
