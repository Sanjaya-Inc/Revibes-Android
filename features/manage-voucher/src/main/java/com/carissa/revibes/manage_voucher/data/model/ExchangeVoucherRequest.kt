package com.carissa.revibes.manage_voucher.data.model

import androidx.annotation.Keep
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Keep
data class CreateExchangeVoucherRequest(
    @EncodeDefault val type: String = "voucher",
    val sourceId: String,
    @EncodeDefault val description: String = "",
    val prices: List<ExchangePrice>,
    @EncodeDefault val quota: Int = -1,
    val endedAt: String? = null
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Keep
data class ExchangePrice(
    val amount: Int,
    @EncodeDefault val currency: String = "revibe-point"
)

@Serializable
@Keep
data class CreateExchangeVoucherResponse(
    val code: Int,
    val message: String,
    val data: ExchangeVoucherData,
    val status: String
)

@Serializable
@Keep
data class ExchangeVoucherData(
    val id: String,
    val type: String,
    val sourceId: String,
    val description: String,
    val prices: List<ExchangePrice>,
    val quota: Int,
    val availableAt: String,
    val endedAt: String?,
    val isAvailable: Boolean,
    val createdAt: String,
    val updatedAt: String
)
