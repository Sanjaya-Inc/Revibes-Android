package com.carissa.revibes.point.data.mapper

import com.carissa.revibes.point.data.model.DailyReward
import com.carissa.revibes.point.domain.model.DailyRewardData

fun List<DailyReward>.toDailyRewardDataList(): List<DailyRewardData> {
    return map { it.toDailyRewardData() }
}

fun DailyReward.toDailyRewardData(): DailyRewardData {
    return DailyRewardData(
        id = id,
        index = index,
        amount = amount,
        createdAt = createdAt,
        claimedAt = claimedAt
    )
}
