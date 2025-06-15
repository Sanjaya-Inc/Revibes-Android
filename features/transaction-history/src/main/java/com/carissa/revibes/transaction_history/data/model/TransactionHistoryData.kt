package com.carissa.revibes.transaction_history.data.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Stable
data class TransactionHistoryData(
    val id: String,
    val title: String,
    val imageUrl: String,
    val type: Type,
    val date: String,
    val validUntil: String,
    val time: String,
    val waterDays: Int,
    val coinReceive: Int,
    val coinPrice: Int,
    val validatorName: String,
    val location: String,
    val status: Status,
    val items: PersistentList<String>
) {
    enum class Type {
        WATER_DAILY, MISSION, REVIBES, COUPONS
    }
    enum class Status {
        SUCCESS, COMPLETED, PROCESS
    }

    companion object {
        fun dummy() = TransactionHistoryData(
            id = "1",
            title = "Water Daily",
            imageUrl = "https://via.placeholder.com/150",
            type = Type.WATER_DAILY,
            date = "2022-01-01",
            validUntil = "2022-01-31",
            time = "12:00 PM",
            waterDays = 1,
            coinPrice = 100,
            coinReceive = 100,
            validatorName = "Validator Name",
            location = "Location",
            items = persistentListOf("Item 1", "Item 2", "Item 3"),
            status = Status.SUCCESS
        )
    }
}
