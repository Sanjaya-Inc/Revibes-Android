package com.carissa.revibes.point.data

import com.carissa.revibes.core.data.utils.BaseRepository
import com.carissa.revibes.point.data.mapper.toDailyRewardDataList
import com.carissa.revibes.point.data.mapper.toDomain
import com.carissa.revibes.point.data.model.NewsData
import com.carissa.revibes.point.data.remote.PointRemoteApi
import com.carissa.revibes.point.domain.model.Mission
import com.carissa.revibes.point.domain.model.PointHistory
import com.carissa.revibes.point.presentation.screen.DailyReward
import org.koin.core.annotation.Single

@Single
class PointRepository(
    private val remoteApi: PointRemoteApi
) : BaseRepository() {

    suspend fun getDailyRewards(): List<DailyReward> {
        return execute { remoteApi.getDailyRewards().data.toDailyRewardDataList() }
    }

    suspend fun claimDailyReward() {
        return execute { remoteApi.claimDailyReward() }
    }

    suspend fun getMissions(): List<Mission> {
        return execute {
            val response = remoteApi.getMissions()
            response.data.items.map { it.toDomain() }
        }
    }

    suspend fun claimMission(id: String) {
        execute { remoteApi.claimMission(id) }
    }

    suspend fun getDailyNews(): NewsData? {
        return execute { remoteApi.getDailyNews().data }
    }

    suspend fun getPointHistories(): List<PointHistory> {
        return execute {
            remoteApi.getPointHistories().data.items.map { item ->
                PointHistory(
                    id = item.id,
                    timestamp = item.timestamp,
                    sourceType = item.sourceType,
                    symbol = item.symbol,
                    value = item.value
                )
            }
        }
    }
}
