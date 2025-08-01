package com.carissa.revibes.manage_users.data.remote

import com.carissa.revibes.manage_users.data.model.AddPointsRequest
import com.carissa.revibes.manage_users.data.model.AddPointsResponse
import com.carissa.revibes.manage_users.data.model.CreateUserRequest
import com.carissa.revibes.manage_users.data.model.CreateUserResponse
import com.carissa.revibes.manage_users.data.model.UserDetailResponse
import com.carissa.revibes.manage_users.data.model.UserListResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Single

interface ManageUsersRemoteApi {
    @GET("users")
    suspend fun getUserList(
        @Query("limit") limit: Int = 2,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortOrder") sortOrder: String = "desc",
        @Query("lastDocId") lastDocId: String? = null,
        @Query("direction") direction: String = "next"
    ): UserListResponse

    @GET("users/{id}")
    suspend fun getUserDetail(
        @Path("id") id: String
    ): UserDetailResponse

    @PATCH("users/{id}/points/add")
    suspend fun addPointsToUser(
        @Path("id") id: String,
        @Body request: AddPointsRequest
    ): AddPointsResponse

    @POST("users")
    suspend fun createUser(
        @Body request: CreateUserRequest
    ): CreateUserResponse
}

@Single
internal class ManageUsersRemoteApiImpl(ktorfit: Ktorfit) :
    ManageUsersRemoteApi by ktorfit.createManageUsersRemoteApi()
