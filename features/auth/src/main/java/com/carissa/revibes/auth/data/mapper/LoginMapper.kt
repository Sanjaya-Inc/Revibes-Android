package com.carissa.revibes.auth.data.mapper

import com.carissa.revibes.auth.data.model.LoginResponse
import com.carissa.revibes.auth.domain.model.AuthTokens
import com.carissa.revibes.auth.domain.model.AuthUser
import com.carissa.revibes.auth.domain.model.LoginResult

internal fun LoginResponse.toDomain(): LoginResult {
    return LoginResult(
        user = data.user.toDomain(),
        tokens = data.tokens.toDomain()
    )
}

private fun com.carissa.revibes.auth.data.model.User.toDomain(): AuthUser {
    return AuthUser(
        id = id,
        role = role,
        displayName = displayName,
        createdAt = createdAt,
        updatedAt = updatedAt,
        verified = verified
    )
}

private fun com.carissa.revibes.auth.data.model.Tokens.toDomain(): AuthTokens {
    return AuthTokens(
        accessToken = accessToken,
        accessTokenExpiresAt = accessTokenExpiresAt,
        refreshToken = refreshToken,
        refreshTokenExpiresAt = refreshTokenExpiresAt
    )
}
