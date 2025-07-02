package com.carissa.revibes.home.data.mapper

import com.carissa.revibes.core.data.user.model.UserData
import com.carissa.revibes.home.data.model.UserMeResponse

internal fun UserMeResponse.toUserData(): UserData {
    return UserData(
        name = data.displayName,
        email = data.email,
        phoneNumber = data.phoneNumber,
        profilePictureUrl = "", // API doesn't provide profile picture URL
        coins = data.points,
        role = data.role,
        createdAt = data.createdAt,
        lastClaimedDate = data.lastClaimedDate
    )
}
