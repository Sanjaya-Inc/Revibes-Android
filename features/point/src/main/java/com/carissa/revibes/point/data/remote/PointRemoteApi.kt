package com.carissa.revibes.point.data.remote

import com.carissa.revibes.point.data.model.DailyRewardResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import org.koin.core.annotation.Single

interface PointRemoteApi {
    @GET("me/daily-rewards")
    suspend fun getDailyRewards(): DailyRewardResponse

    @PATCH("me/daily-rewards")
    suspend fun claimDailyReward()
}

@Single
internal class PointRemoteApiImpl(ktorfit: Ktorfit) :
    PointRemoteApi by ktorfit.createPointRemoteApi()
