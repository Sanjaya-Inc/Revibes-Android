package com.carissa.revibes.manage_users.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UserListResponse(
    val code: Int,
    val message: String,
    val data: UserListResponseData,
    val status: String
)

@Keep
@Serializable
data class UserListResponseData(
    val items: List<UserData>,
    val pagination: PaginationData
)

@Keep
@Serializable
data class UserData(
    val id: String,
    val role: String,
    val displayName: String,
    val createdAt: String,
    val updatedAt: String
)

@Keep
@Serializable
data class PaginationData(
    val limit: Int,
    val sortBy: String,
    val sortOrder: String,
    val lastDocId: String? = null,
    val firstDocId: String? = null,
    val direction: String,
    val hasMoreNext: Boolean,
    val hasMorePrev: Boolean
)
