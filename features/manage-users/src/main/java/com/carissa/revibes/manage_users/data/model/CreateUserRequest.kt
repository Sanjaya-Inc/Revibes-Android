package com.carissa.revibes.manage_users.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CreateUserRequest(
    val displayName: String,
    val email: String,
    val phone: String,
    val role: String,
    val password: String,
)

@Keep
@Serializable
data class CreateUserResponse(
    val code: Int,
    val message: String,
    val data: CreateUserData,
    val status: String
)

@Keep
@Serializable
data class CreateUserData(
    val displayName: String,
    val email: String,
    val phoneNumber: String,
    val role: String,
    val points: Int,
    val createdAt: String,
)
