package com.carissa.revibes.core.data.user.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Keep
@Stable
@Serializable
data class UserData(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val profilePictureUrl: String,
    val coins: Int,
    val role: String,
    val createdAt: String,
    val lastClaimedDate: String?
) {
    companion object {
        fun dummy() = UserData(
            name = "John Doe",
            email = "johndoe@example.com",
            phoneNumber = "123-456-7890",
            profilePictureUrl = "https://example.com/profile.jpg",
            coins = 100,
            role = "user",
            createdAt = "2025-06-25T10:37:17.180Z",
            lastClaimedDate = null
        )
    }
}
