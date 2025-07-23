package com.carissa.revibes.exchange_points.data.remote

import com.carissa.revibes.exchange_points.data.model.ExchangeDataResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Single
interface ExchangePointsRemoteApi {
    @GET("exchanges")
    suspend fun getVouchers(
        @Query("limit") limit: Int = 10,
        @Query("types") types: String = "voucher"
    ): ExchangeDataResponse
}

@Single
internal class ExchangePointsRemoteApiImpl(ktorfit: Ktorfit) :
    ExchangePointsRemoteApi by ktorfit.createExchangePointsRemoteApi()
