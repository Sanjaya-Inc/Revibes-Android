package com.carissa.revibes.point.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DailyRewardResponse(
    val code: Int,
    val message: String,
    val data: List<DailyRewardData>,
    val status: String
)

@Keep
@Serializable
data class DailyRewardData(
    val id: String,
    val index: Int,
    val amount: Int,
    val createdAt: String,
    val claimedAt: String?
)
