package com.carissa.revibes.auth.presentation.mapper

import com.carissa.revibes.auth.domain.model.AuthUser
import com.carissa.revibes.core.data.user.model.UserData

internal fun AuthUser.toUserDataWithEmail(email: String): UserData {
    return UserData(
        name = displayName,
        email = email,
        phoneNumber = "",
        profilePictureUrl = "",
        coins = 0,
        role = role,
        createdAt = createdAt,
        lastClaimedDate = null,
        verified = verified
    )
}

internal fun AuthUser.toUserDataWithPhone(phone: String): UserData {
    return UserData(
        name = displayName,
        email = "",
        phoneNumber = phone,
        profilePictureUrl = "",
        coins = 0,
        role = role,
        createdAt = createdAt,
        lastClaimedDate = null,
        verified = verified
    )
}
