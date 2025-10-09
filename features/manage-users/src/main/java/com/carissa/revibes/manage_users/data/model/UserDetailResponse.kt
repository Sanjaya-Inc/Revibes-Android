package com.carissa.revibes.manage_users.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UserDetailResponse(
    val code: Int,
    val message: String,
    val data: UserDetailData,
    val status: String
)

@Keep
@Serializable
data class UserDetailData(
//    val id: String,
    val displayName: String,
    val email: String,
    val phoneNumber: String,
    val role: String,
    val points: Int,
    val createdAt: String,
//    val updatedAt: String,
//    val isActive: Boolean,
//    val address: String? = null,
//    val profileImage: String? = null
)
