package com.carissa.revibes.manage_transaction.data

import com.carissa.revibes.manage_transaction.data.mapper.toDomain
import com.carissa.revibes.manage_transaction.data.model.RejectTransactionRequest
import com.carissa.revibes.manage_transaction.data.remote.ManageTransactionRemoteApi
import com.carissa.revibes.manage_transaction.domain.model.ManageTransactionDomain
import com.carissa.revibes.manage_transaction.domain.model.TransactionDetailDomain
import com.carissa.revibes.manage_transaction.presentation.screen.TransactionStatus
import org.koin.core.annotation.Single

@Single
internal class ManageTransactionRepository(
    private val remoteApi: ManageTransactionRemoteApi
) {
    suspend fun getTransactions(
        status: TransactionStatus,
        limit: Int = 10,
        sortBy: String = "createdAt",
        sortOrder: String = "desc",
        lastDocId: String? = null,
        direction: String = "next"
    ): List<ManageTransactionDomain> {
        val statuses = when (status) {
            TransactionStatus.ALL -> listOf("submitted", "rejected", "completed")
            else -> listOf(status.queryParam)
        }

        return remoteApi.getTransactions(
            limit = limit,
            sortBy = sortBy,
            sortOrder = sortOrder,
            lastDocId = lastDocId,
            direction = direction,
            statuses = statuses
        ).data.items.map { it.toDomain() }
    }

    suspend fun getPendingTransactions(
        limit: Int = 10,
        sortBy: String = "createdAt",
        sortOrder: String = "desc",
        lastDocId: String? = null,
        direction: String = "next"
    ): List<ManageTransactionDomain> {
        return remoteApi.getPendingTransactions(
            limit = limit,
            sortBy = sortBy,
            sortOrder = sortOrder,
            lastDocId = lastDocId,
            direction = direction
        ).data.items.map { it.toDomain() }
    }

    suspend fun getTransactionDetail(id: String): TransactionDetailDomain {
        return remoteApi.getTransactionDetail(id).data.toDomain()
    }

    suspend fun rejectTransaction(id: String, reason: String) {
        remoteApi.rejectTransaction(id, request = RejectTransactionRequest(reason))
    }

    suspend fun completeTransaction(id: String) {
        remoteApi.completeTransaction(id)
    }
}
