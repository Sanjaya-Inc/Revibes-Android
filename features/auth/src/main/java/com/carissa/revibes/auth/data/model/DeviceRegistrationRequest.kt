package com.carissa.revibes.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DeviceRegistrationRequest(
    val deviceToken: String,
    val fcmToken: String
)
