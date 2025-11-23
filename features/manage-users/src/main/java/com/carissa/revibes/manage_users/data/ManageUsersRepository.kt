package com.carissa.revibes.manage_users.data

import com.carissa.revibes.core.data.utils.BaseRepository
import com.carissa.revibes.exchange_points.data.mapper.toUserVoucher
import com.carissa.revibes.exchange_points.domain.model.UserVoucher
import com.carissa.revibes.manage_users.data.mapper.mapToUserRole
import com.carissa.revibes.manage_users.data.mapper.toApiString
import com.carissa.revibes.manage_users.data.mapper.toUserDomain
import com.carissa.revibes.manage_users.data.mapper.toUserDomainList
import com.carissa.revibes.manage_users.data.model.AddPointsRequest
import com.carissa.revibes.manage_users.data.model.CreateUserRequest
import com.carissa.revibes.manage_users.data.model.PaginationData
import com.carissa.revibes.manage_users.data.model.UpdateUserRequest
import com.carissa.revibes.manage_users.data.model.UpdateVerificationRequest
import com.carissa.revibes.manage_users.data.remote.ManageUsersRemoteApi
import com.carissa.revibes.manage_users.domain.model.UserDomain
import org.koin.core.annotation.Single

data class UserListResult(
    val users: List<UserDomain>,
    val pagination: PaginationData
)

@Single
class ManageUsersRepository(
    private val remoteApi: ManageUsersRemoteApi
) : BaseRepository() {

    suspend fun getUserList(
        limit: Int = 2,
        sortBy: String = "createdAt",
        sortOrder: String = "desc",
        lastDocId: String? = null,
        direction: String = "next"
    ): UserListResult {
        return execute {
            val response = remoteApi.getUserList(
                limit = limit,
                sortBy = sortBy,
                sortOrder = sortOrder,
                lastDocId = lastDocId,
                direction = direction
            )

            UserListResult(
                users = response.data.items.toUserDomainList(),
                pagination = response.data.pagination
            )
        }
    }

    suspend fun getUserDetail(id: String): UserDomain {
        return execute {
            val response = remoteApi.getUserDetail(id)
            response.data.toUserDomain(id)
        }
    }

    suspend fun addPointsToUser(id: String, points: Int): UserDomain {
        return execute {
            val request = AddPointsRequest(amount = points)
            remoteApi.addPointsToUser(id, request)
            getUserDetail(id)
        }
    }

    suspend fun createUser(
        name: String,
        email: String,
        phone: String,
        role: String,
        password: String,
    ): UserDomain {
        return execute {
            val request = CreateUserRequest(
                displayName = name,
                email = email,
                phone = phone,
                role = role,
                password = password,
            )
            val response = remoteApi.createUser(request)
            UserDomain(
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
                profileImage = null,
                verified = false
            )
        }
    }

    suspend fun deductPointsFromUser(id: String, points: Int): UserDomain {
        return execute {
            val request = AddPointsRequest(amount = -points) // Use negative value for deduction
            remoteApi.addPointsToUser(id, request)
            getUserDetail(id)
        }
    }

    suspend fun updateUser(
        id: String,
        name: String,
        email: String,
        phone: String,
        role: UserDomain.UserRole
    ): UserDomain {
        return execute {
            val request = UpdateUserRequest(
                displayName = name,
                email = email,
                phoneNumber = phone,
                role = role.toApiString()
            )
            remoteApi.updateUser(id, request)
            getUserDetail(id) // Fetch updated user details
        }
    }

    suspend fun getUserVouchers(id: String): List<UserVoucher> {
        return execute {
            val response = remoteApi.getUserVouchers(id)
            response.data.items.map { it.toUserVoucher() }
        }
    }

    suspend fun redeemVoucher(userId: String, voucherId: String) {
        return execute {
            remoteApi.redeemVoucher(userId, voucherId)
        }
    }

    suspend fun updateVerifyStatue(userId: String, isVerified: Boolean) {
        return execute {
            remoteApi.updateVerifyStatus(userId, UpdateVerificationRequest(isVerified))
        }
    }
}
