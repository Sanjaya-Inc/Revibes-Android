package com.carissa.revibes.manage_transaction.data.remote

import com.carissa.revibes.manage_transaction.data.model.RejectTransactionRequest
import com.carissa.revibes.manage_transaction.data.model.TransactionActionResponse
import com.carissa.revibes.transaction_history.data.model.TransactionDetailResponse
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Single

interface ManageTransactionRemoteApi {
    @GET("logistic-orders")
    suspend fun getPendingTransactions(
        @Query("limit") limit: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortOrder") sortOrder: String = "desc",
        @Query("lastDocId") lastDocId: String? = null,
        @Query("direction") direction: String = "next",
        @Query("statuses") statuses: List<String> = listOf("submitted")
    ): TransactionHistoryResponse

    @GET("logistic-orders/{id}")
    suspend fun getTransactionDetail(
        @Path("id") id: String
    ): TransactionDetailResponse

    @PATCH("logistic-orders/{id}/reject")
    suspend fun rejectTransaction(
        @Path("id") id: String,
        @Body request: RejectTransactionRequest
    ): TransactionActionResponse

    @PATCH("logistic-orders/{id}/complete")
    suspend fun completeTransaction(
        @Path("id") id: String
    ): TransactionActionResponse
}

@Single
internal class ManageTransactionRemoteApiImpl(ktorfit: Ktorfit) :
    ManageTransactionRemoteApi by ktorfit.createManageTransactionRemoteApi()
