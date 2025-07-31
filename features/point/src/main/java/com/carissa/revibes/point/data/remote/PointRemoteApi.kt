package com.carissa.revibes.point.data.remote

import com.carissa.revibes.point.data.model.DailyRewardResponse
import com.carissa.revibes.point.data.remote.response.MissionResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Path
import org.koin.core.annotation.Single

interface PointRemoteApi {
    @GET("me/daily-rewards")
    suspend fun getDailyRewards(): DailyRewardResponse

    @PATCH("me/daily-rewards")
    suspend fun claimDailyReward()

    @GET("me/missions")
    suspend fun getMissions(): MissionResponse

    @PATCH("me/missions/{id}")
    suspend fun claimMission(@Path("id") id: String)
}

@Single
internal class PointRemoteApiImpl(ktorfit: Ktorfit) :
    PointRemoteApi by ktorfit.createPointRemoteApi()
