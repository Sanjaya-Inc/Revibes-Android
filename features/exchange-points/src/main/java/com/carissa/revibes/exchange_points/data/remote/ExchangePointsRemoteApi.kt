package com.carissa.revibes.exchange_points.data.remote

import com.carissa.revibes.exchange_points.data.model.ExchangeDataResponse
import com.carissa.revibes.exchange_points.data.model.PurchaseRequest
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Single

interface ExchangePointsRemoteApi {
    @GET("exchanges")
    suspend fun getVouchers(
        @Query("limit") limit: Int = 10,
        @Query("types") types: String = "voucher"
    ): ExchangeDataResponse

    @POST("exchanges/transactions")
    suspend fun purchaseVoucher(
        @Body purchaseRequest: PurchaseRequest,
        @Header("Content-Type") contentType: String = "application/json",
    )
}

@Single
internal class ExchangePointsRemoteApiImpl(ktorfit: Ktorfit) :
    ExchangePointsRemoteApi by ktorfit.createExchangePointsRemoteApi()
