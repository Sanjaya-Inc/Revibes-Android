package com.carissa.revibes.manage_users.domain.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Stable
data class UserDomain(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val points: Int,
    val createdAt: String,
    val updatedAt: String? = null,
    val isActive: Boolean,
    val address: String? = null,
    val profileImage: String? = null
) {
    enum class UserRole {
        ADMIN, USER
    }

    companion object {
        fun dummy() = UserDomain(
            id = "1",
            name = "John Doe",
            email = "john.doe@example.com",
            phone = "+1234567890",
            role = UserRole.USER,
            points = 1500,
            createdAt = "2024-01-01T00:00:00.000Z",
            updatedAt = "2024-01-01T00:00:00.000Z",
            isActive = true,
            address = "123 Main St, City, Country",
            profileImage = null
        )
    }
}
