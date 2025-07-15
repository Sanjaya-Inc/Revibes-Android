package com.carissa.revibes.transaction_history.data.remote

import com.carissa.revibes.transaction_history.data.model.TransactionDetailResponse
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Single

interface TransactionHistoryRemoteApi {
    @GET("logistic-orders")
    suspend fun getTransactionHistory(
        @Query("limit") limit: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortOrder") sortOrder: String = "desc",
        @Query("lastDocId") lastDocId: String? = null,
        @Query("direction") direction: String = "next",
        @Query("statuses") statuses: List<String>? = null
    ): TransactionHistoryResponse

    @GET("logistic-orders/{id}")
    suspend fun getTransactionDetail(
        @Path("id") id: String
    ): TransactionDetailResponse
}

@Single
internal class TransactionHistoryRemoteApiImpl(ktorfit: Ktorfit) :
    TransactionHistoryRemoteApi by ktorfit.createTransactionHistoryRemoteApi()
