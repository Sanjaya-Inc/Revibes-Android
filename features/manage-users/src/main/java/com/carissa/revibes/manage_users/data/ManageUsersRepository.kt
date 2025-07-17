package com.carissa.revibes.manage_users.data

import com.carissa.revibes.manage_users.data.mapper.mapToUserRole
import com.carissa.revibes.manage_users.data.mapper.toUserDomain
import com.carissa.revibes.manage_users.data.mapper.toUserDomainList
import com.carissa.revibes.manage_users.data.model.AddPointsRequest
import com.carissa.revibes.manage_users.data.model.CreateUserRequest
import com.carissa.revibes.manage_users.data.model.PaginationData
import com.carissa.revibes.manage_users.data.remote.ManageUsersRemoteApi
import com.carissa.revibes.manage_users.domain.model.UserDomain
import org.koin.core.annotation.Single

data class UserListResult(
    val users: List<UserDomain>,
    val pagination: PaginationData
)

interface ManageUsersRepository {
    suspend fun getUserList(
        limit: Int = 2,
        sortBy: String = "createdAt",
        sortOrder: String = "desc",
        lastDocId: String? = null,
        direction: String = "next"
    ): UserListResult

    suspend fun getUserDetail(id: String): UserDomain

    suspend fun addPointsToUser(id: String, points: Int): UserDomain

    suspend fun createUser(
        name: String,
        email: String,
        phone: String,
        role: String,
        password: String,
    ): UserDomain
}

@Single
internal class ManageUsersRepositoryImpl(
    private val remoteApi: ManageUsersRemoteApi
) : ManageUsersRepository {

    override suspend fun getUserList(
        limit: Int,
        sortBy: String,
        sortOrder: String,
        lastDocId: String?,
        direction: String
    ): UserListResult {
        val response = remoteApi.getUserList(
            limit = limit,
            sortBy = sortBy,
            sortOrder = sortOrder,
            lastDocId = lastDocId,
            direction = direction
        )

        return UserListResult(
            users = response.data.items.toUserDomainList(),
            pagination = response.data.pagination
        )
    }

    override suspend fun getUserDetail(id: String): UserDomain {
        val response = remoteApi.getUserDetail(id)
        return response.data.toUserDomain(id)
    }

    override suspend fun addPointsToUser(id: String, points: Int): UserDomain {
        val request = AddPointsRequest(amount = points)
        remoteApi.addPointsToUser(id, request)
            .also { println("ketai: Added points to user $id: $points") }
        return getUserDetail(id).also { println("ketai: User after adding points: $it") }
    }

    override suspend fun createUser(
        name: String,
        email: String,
        phone: String,
        role: String,
        password: String,
    ): UserDomain {
        val request = CreateUserRequest(
            displayName = name,
            email = email,
            phone = phone,
            role = role,
            password = password,
        )
        val response = remoteApi.createUser(request)
        return UserDomain(
            id = "",
            name = response.data.displayName,
            email = response.data.email,
            phone = response.data.phoneNumber,
            role = mapToUserRole(response.data.role),
            points = response.data.points,
            createdAt = response.data.createdAt,
            updatedAt = response.data.createdAt,
            isActive = false,
            address = null,
            profileImage = null
        )
    }
}
