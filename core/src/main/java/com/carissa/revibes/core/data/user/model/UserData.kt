package com.carissa.revibes.core.data.user.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

@Keep
@Stable
data class UserData(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val profilePictureUrl: String,
    val coins: Int
) {
    companion object {
        fun dummy() = UserData(
            name = "John Doe",
            email = "johndoe@example.com",
            phoneNumber = "123-456-7890",
            profilePictureUrl = "https://example.com/profile.jpg",
            coins = 100
        )
    }
}
