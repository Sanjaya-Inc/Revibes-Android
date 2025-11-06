package com.carissa.revibes.manage_users.data.mapper

import com.carissa.revibes.manage_users.data.model.UserData
import com.carissa.revibes.manage_users.data.model.UserDetailData
import com.carissa.revibes.manage_users.domain.model.UserDomain

fun List<UserData>.toUserDomainList(): List<UserDomain> {
    return map { it.toUserDomain() }
}

fun UserData.toUserDomain(): UserDomain {
    return UserDomain(
        id = id,
        name = displayName,
        email = "-",
        phone = "-",
        role = mapToUserRole(role),
        points = 0,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isActive = true,
        address = null,
        profileImage = null,
        verified = verified
    )
}

fun UserDetailData.toUserDomain(id: String): UserDomain {
    return UserDomain(
        id = id,
        name = displayName,
        email = email,
        phone = phoneNumber,
        role = mapToUserRole(role),
        points = points,
        createdAt = createdAt,
        updatedAt = null,
        isActive = true,
        address = null,
        profileImage = null,
        verified = verified
    )
}

fun mapToUserRole(apiRole: String): UserDomain.UserRole {
    return when (apiRole.lowercase()) {
        "admin" -> UserDomain.UserRole.ADMIN
        "user" -> UserDomain.UserRole.USER
        else -> UserDomain.UserRole.USER
    }
}

fun UserDomain.UserRole.toApiString(): String {
    return when (this) {
        UserDomain.UserRole.ADMIN -> "admin"
        UserDomain.UserRole.USER -> "user"
    }
}
