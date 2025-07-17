package com.carissa.revibes.manage_users.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AddPointsRequest(
    val amount: Int,
)

@Keep
@Serializable
data class AddPointsResponse(
    val code: Int,
    val message: String,
    val status: String
)
