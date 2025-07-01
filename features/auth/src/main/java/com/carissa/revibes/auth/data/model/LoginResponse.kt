package com.carissa.revibes.auth.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LoginResponse(
    val code: Int,
    val message: String,
    val data: LoginData,
    val status: String
)

@Keep
@Serializable
data class LoginData(
    val user: User,
    val tokens: Tokens
)

@Keep
@Serializable
data class User(
    val id: String,
    val role: String,
    val displayName: String,
    val createdAt: String,
    val updatedAt: String
)

@Keep
@Serializable
data class Tokens(
    val accessToken: String,
    val accessTokenExpiresAt: String,
    val refreshToken: String,
    val refreshTokenExpiresAt: String
)
