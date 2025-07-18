package com.carissa.revibes.transaction_history.data.mapper

import com.carissa.revibes.transaction_history.data.model.LogisticOrderData
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryData
import kotlinx.collections.immutable.toPersistentList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun List<LogisticOrderData>.toTransactionHistoryDataList(): List<TransactionHistoryData> {
    return map { it.toTransactionHistoryData() }
}

fun LogisticOrderData.toTransactionHistoryData(): TransactionHistoryData {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val displayDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val createdDate = try {
        dateFormat.parse(createdAt) ?: Date()
    } catch (e: Exception) {
        Date()
    }

    val imageUrl = items.firstOrNull()?.media?.firstOrNull()?.downloadUri ?: ""
    val itemNames = items.map { it.name.ifEmpty { "Unknown Item" } }

    return TransactionHistoryData(
        id = id,
        title = when (type) {
            "drop-off" -> "Drop Off - ${name.ifEmpty { "Anonymous" }}"
            else -> type.replaceFirstChar { it.uppercase() }
        },
        imageUrl = imageUrl,
        type = mapToTransactionType(type),
        date = displayDateFormat.format(createdDate),
        validUntil = "", // Not applicable for logistic orders
        time = timeFormat.format(createdDate),
        waterDays = 0, // Not applicable for logistic orders
        coinReceive = totalPoint,
        coinPrice = totalPoint,
        validatorName = name.ifEmpty { "-" },
        location = this.store.address.ifEmpty { "Unknown Location" },
        status = mapToTransactionStatus(status),
        items = itemNames.toPersistentList()
    )
}

private fun mapToTransactionType(apiType: String): TransactionHistoryData.Type {
    return when (apiType.lowercase()) {
        "drop-off" -> TransactionHistoryData.Type.REVIBES
        else -> TransactionHistoryData.Type.MISSION
    }
}

private fun mapToTransactionStatus(apiStatus: String): TransactionHistoryData.Status {
    return when (apiStatus.lowercase()) {
        "completed" -> TransactionHistoryData.Status.COMPLETED
        "submitted" -> TransactionHistoryData.Status.PROCESS
        "draft" -> TransactionHistoryData.Status.PROCESS
        else -> TransactionHistoryData.Status.SUCCESS
    }
}
