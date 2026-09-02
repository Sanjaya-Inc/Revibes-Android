package com.carissa.revibes.home_admin.data.remote

import com.carissa.revibes.home_admin.data.model.CreateStoreRequest
import com.carissa.revibes.home_admin.data.model.StoreDetailResponse
import com.carissa.revibes.home_admin.data.model.StoreListResponse
import com.carissa.revibes.home_admin.data.model.UpdateStoreRequest
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import org.koin.core.annotation.Single

interface StoreRemoteApi {
    @GET("stores")
    suspend fun getStores(): StoreListResponse

    @POST("stores")
    @Headers("Content-Type: application/json")
    suspend fun createStore(@Body request: CreateStoreRequest): StoreDetailResponse

    @PUT("stores/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateStore(
        @Path("id") id: String,
        @Body request: UpdateStoreRequest
    ): StoreDetailResponse
}

@Single
internal class StoreRemoteApiImpl(ktorfit: Ktorfit) :
    StoreRemoteApi by ktorfit.createStoreRemoteApi()
