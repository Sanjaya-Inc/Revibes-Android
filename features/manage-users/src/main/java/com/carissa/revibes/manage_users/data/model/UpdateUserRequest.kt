package com.carissa.revibes.manage_users.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UpdateUserRequest(
    val email: String? = null,
    val displayName: String? = null,
    val phoneNumber: String? = null,
    val role: String? = null,
    val points: Long? = null
)

@Keep
@Serializable
data class UpdateUserResponse(
    val code: Int,
    val message: String,
    val status: String
)
