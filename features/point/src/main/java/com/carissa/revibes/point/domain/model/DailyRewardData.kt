package com.carissa.revibes.point.domain.model

data class DailyRewardData(
    val id: String,
    val index: Int,
    val amount: Int,
    val createdAt: String,
    val claimedAt: String?
)
