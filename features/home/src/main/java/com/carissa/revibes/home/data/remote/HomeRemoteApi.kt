package com.carissa.revibes.home.data.remote

import com.carissa.revibes.home.data.model.BannerResponse
import com.carissa.revibes.home.data.model.UserMeResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import org.koin.core.annotation.Single

interface HomeRemoteApi {
    @GET("banners")
    suspend fun getBanners(): BannerResponse

    @GET("users/me")
    suspend fun getUserMe(): UserMeResponse
}

@Single
internal class HomeRemoteApiImpl(ktorfit: Ktorfit) :
    HomeRemoteApi by ktorfit.createHomeRemoteApi()
