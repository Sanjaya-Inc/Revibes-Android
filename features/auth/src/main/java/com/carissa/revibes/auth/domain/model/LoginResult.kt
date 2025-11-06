package com.carissa.revibes.auth.domain.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

@Keep
@Stable
data class LoginResult(
    val user: AuthUser,
    val tokens: AuthTokens
)

@Keep
@Stable
data class AuthUser(
    val id: String,
    val role: String,
    val displayName: String,
    val createdAt: String,
    val updatedAt: String,
    val verified: Boolean
)

@Keep
@Stable
data class AuthTokens(
    val accessToken: String,
    val accessTokenExpiresAt: String,
    val refreshToken: String,
    val refreshTokenExpiresAt: String
)
