package com.carissa.revibes.home.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UserMeResponse(
    val code: Int,
    val message: String,
    val data: UserMeData,
    val status: String
)

@Keep
@Serializable
data class UserMeData(
    val role: String,
    val createdAt: String,
    val displayName: String,
    val email: String,
    val phoneNumber: String,
    val points: Int,
    val verified: Boolean
)
