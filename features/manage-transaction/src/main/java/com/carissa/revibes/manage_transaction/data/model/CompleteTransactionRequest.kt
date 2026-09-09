package com.carissa.revibes.manage_transaction.data.model

import androidx.annotation.Keep
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Keep
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class CompleteTransactionRequest(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val customTotalPoint: Int? = null
)
