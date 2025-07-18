package com.carissa.revibes.manage_voucher.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse(
    val code: Int,
    val message: String,
    val status: String
)
