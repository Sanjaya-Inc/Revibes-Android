package com.carissa.revibes.transaction_history.domain.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import com.carissa.revibes.core.presentation.components.components.TransactionItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

@Keep
@Stable
data class TransactionDetailDomain(
    val id: String,
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val country: String,
    val address: String,
    val addressDetail: String?,
    val postalCode: String,
    val storeLocation: String,
    val status: String,
    val totalPoint: Int,
    val items: ImmutableList<TransactionItem>,
    val itemPoints: ImmutableMap<String, Int>,
    val locationAddress: String,
    val formattedDate: String,
    val formattedStatus: String
)
