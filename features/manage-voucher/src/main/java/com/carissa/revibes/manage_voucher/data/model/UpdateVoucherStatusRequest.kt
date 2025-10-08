package com.carissa.revibes.manage_voucher.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UpdateVoucherStatusRequest(
    val isAvailable: Boolean
)
