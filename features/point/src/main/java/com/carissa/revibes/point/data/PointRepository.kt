package com.carissa.revibes.point.data

import com.carissa.revibes.point.data.mapper.toDailyRewardDataList
import com.carissa.revibes.point.data.mapper.toDomain
import com.carissa.revibes.point.data.remote.PointRemoteApi
import com.carissa.revibes.point.domain.model.Mission
import com.carissa.revibes.point.presentation.screen.DailyReward
import org.koin.core.annotation.Single

interface PointRepository {
    suspend fun getDailyRewards(): List<DailyReward>
    suspend fun claimDailyReward()
    suspend fun getMissions(): List<Mission>
    suspend fun claimMission(id: String)
}

@Single
internal class PointRepositoryImpl(
    private val remoteApi: PointRemoteApi
) : PointRepository {

    override suspend fun getDailyRewards(): List<DailyReward> {
        return remoteApi.getDailyRewards().data.toDailyRewardDataList()
    }

    override suspend fun claimDailyReward() {
        return remoteApi.claimDailyReward()
    }

    override suspend fun getMissions(): List<Mission> {
        val response = remoteApi.getMissions()
        return response.data.items.map { it.toDomain() }
    }

    override suspend fun claimMission(id: String) {
        remoteApi.claimMission(id)
    }
}
