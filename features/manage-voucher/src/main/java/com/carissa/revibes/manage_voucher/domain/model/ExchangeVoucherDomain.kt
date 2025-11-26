package com.carissa.revibes.manage_voucher.domain.model

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Stable
data class ExchangeVoucherDomain(
    val id: String,
    val type: String,
    val sourceId: String,
    val description: String,
    val prices: List<ExchangePriceDomain>,
    val quota: Int,
    val availableAt: String,
    val endedAt: String?,
    val isAvailable: Boolean,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
@Keep
@Stable
data class ExchangePriceDomain(
    val amount: Int,
    val currency: String
)
