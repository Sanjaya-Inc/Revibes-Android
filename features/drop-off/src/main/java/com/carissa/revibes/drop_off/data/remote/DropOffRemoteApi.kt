package com.carissa.revibes.drop_off.data.remote

import com.carissa.revibes.drop_off.data.model.EstimatePointRequest
import com.carissa.revibes.drop_off.data.model.EstimatePointResponse
import com.carissa.revibes.drop_off.data.model.LogisticOrderItemResponse
import com.carissa.revibes.drop_off.data.model.LogisticOrderResponse
import com.carissa.revibes.drop_off.data.model.PresignedUrlRequest
import com.carissa.revibes.drop_off.data.model.PresignedUrlResponse
import com.carissa.revibes.drop_off.data.model.StoreResponse
import com.carissa.revibes.drop_off.data.model.SubmitOrderRequest
import com.carissa.revibes.drop_off.data.model.SubmitOrderResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Single

interface DropOffRemoteApi {
    @GET("stores")
    suspend fun getStores(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ): StoreResponse

    @POST("logistic-orders")
    suspend fun createLogisticOrder(): LogisticOrderResponse

    @POST("logistic-orders/{orderId}/items")
    suspend fun createLogisticOrderItem(@Path("orderId") orderId: String): LogisticOrderItemResponse

    @POST("logistic-orders/{orderId}/items/{itemId}/media/presigned-url/")
    suspend fun getPresignedUrl(
        @Path("orderId") orderId: String,
        @Path("itemId") itemId: String,
        @Body request: PresignedUrlRequest
    ): PresignedUrlResponse

    @POST("logistic-orders/estimate-point")
    suspend fun estimatePoint(@Body request: EstimatePointRequest): EstimatePointResponse

    @PATCH("logistic-orders/{id}/submit")
    suspend fun submitOrder(
        @Path("id") id: String,
        @Body request: SubmitOrderRequest
    ): SubmitOrderResponse
}

@Single
internal class DropOffRemoteApiImpl(ktorfit: Ktorfit) :
    DropOffRemoteApi by ktorfit.createDropOffRemoteApi()
