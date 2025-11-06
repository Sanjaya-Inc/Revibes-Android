package com.carissa.revibes.manage_users.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UpdateVerificationRequest(
    val verified: Boolean
)
