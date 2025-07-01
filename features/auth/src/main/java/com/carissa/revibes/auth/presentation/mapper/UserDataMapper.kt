package com.carissa.revibes.auth.presentation.mapper

import com.carissa.revibes.auth.domain.model.AuthUser
import com.carissa.revibes.core.data.user.model.UserData

internal fun AuthUser.toUserData(email: String): UserData {
    return UserData(
        name = displayName,
        email = email,
        phoneNumber = "",
        profilePictureUrl = "",
        coins = 0
    )
}
