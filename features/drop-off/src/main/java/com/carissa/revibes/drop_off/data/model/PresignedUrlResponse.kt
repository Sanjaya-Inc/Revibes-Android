package com.carissa.revibes.drop_off.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PresignedUrlResponse(
    val code: Int,
    val message: String,
    val data: PresignedUrlData,
    val status: String
)

@Keep
@Serializable
data class PresignedUrlData(
    val uploadUrl: String,
    val expiredAt: Long
)

@Keep
@Serializable
data class PresignedUrlRequest(
    val contentType: String
)
