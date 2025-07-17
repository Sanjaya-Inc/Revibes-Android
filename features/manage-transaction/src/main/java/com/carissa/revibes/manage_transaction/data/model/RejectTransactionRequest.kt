package com.carissa.revibes.manage_transaction.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class RejectTransactionRequest(
    val reason: String? = null
)
