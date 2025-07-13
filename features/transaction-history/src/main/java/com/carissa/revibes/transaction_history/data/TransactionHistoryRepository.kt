package com.carissa.revibes.transaction_history.data

import com.carissa.revibes.transaction_history.data.mapper.toTransactionHistoryDataList
import com.carissa.revibes.transaction_history.data.model.PaginationData
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryData
import com.carissa.revibes.transaction_history.data.remote.TransactionHistoryRemoteApi
import org.koin.core.annotation.Single

data class TransactionHistoryResult(
    val items: List<TransactionHistoryData>,
    val pagination: PaginationData
)

interface TransactionHistoryRepository {
    suspend fun getTransactionHistory(
        limit: Int = 10,
        sortBy: String = "createdAt",
        sortOrder: String = "desc",
        lastDocId: String? = null,
        direction: String = "next"
    ): TransactionHistoryResult
}

@Single
internal class TransactionHistoryRepositoryImpl(
    private val remoteApi: TransactionHistoryRemoteApi
) : TransactionHistoryRepository {

    override suspend fun getTransactionHistory(
        limit: Int,
        sortBy: String,
        sortOrder: String,
        lastDocId: String?,
        direction: String
    ): TransactionHistoryResult {
        val response = remoteApi.getTransactionHistory(
            limit = limit,
//            sortBy = sortBy, // Uncomment after backend fixed
//            sortOrder = sortOrder,
//            lastDocId = lastDocId,
//            direction = direction
        )

        return TransactionHistoryResult(
            items = response.data.items.toTransactionHistoryDataList(),
            pagination = response.data.pagination
        )
    }
}
