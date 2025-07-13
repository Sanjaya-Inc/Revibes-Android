package com.carissa.revibes.point.data.mapper

import com.carissa.revibes.point.data.model.DailyRewardData
import com.carissa.revibes.point.presentation.screen.DailyReward

fun List<DailyRewardData>.toDailyRewardDataList(): List<DailyReward> {
    return map { it.toDailyPoint() }
}

fun DailyRewardData.toDailyPoint(): DailyReward {
    return DailyReward(
        id = id,
        dayIndex = index,
        amount = amount,
        claimedAt = claimedAt
    )
}
