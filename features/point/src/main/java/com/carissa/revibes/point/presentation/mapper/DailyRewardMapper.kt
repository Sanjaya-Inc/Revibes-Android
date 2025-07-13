package com.carissa.revibes.point.presentation.mapper

import com.carissa.revibes.point.domain.model.DailyRewardData
import com.carissa.revibes.point.presentation.screen.DailyReward

fun List<DailyRewardData>.toDailyPointList(): List<DailyReward> {
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
