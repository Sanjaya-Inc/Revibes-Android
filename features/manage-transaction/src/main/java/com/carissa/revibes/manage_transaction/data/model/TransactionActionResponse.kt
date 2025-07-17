package com.carissa.revibes.manage_transaction.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TransactionActionResponse(
    val code: Int,
    val message: String,
    val status: String
)
