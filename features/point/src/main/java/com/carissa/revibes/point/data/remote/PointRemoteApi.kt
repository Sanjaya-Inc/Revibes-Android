package com.carissa.revibes.point.data.remote

import com.carissa.revibes.point.data.model.DailyRewardResponse
import com.carissa.revibes.point.data.model.NewsResponse
import com.carissa.revibes.point.data.model.PointHistoryResponse
import com.carissa.revibes.point.data.remote.response.MissionResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
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

    @GET("news")
    suspend fun getDailyNews(): NewsResponse

    @GET("me/points/histories")
    suspend fun getPointHistories(@Query("limit") limit: Int = 20): PointHistoryResponse
}

@Single
internal class PointRemoteApiImpl(ktorfit: Ktorfit) :
    PointRemoteApi by ktorfit.createPointRemoteApi()
