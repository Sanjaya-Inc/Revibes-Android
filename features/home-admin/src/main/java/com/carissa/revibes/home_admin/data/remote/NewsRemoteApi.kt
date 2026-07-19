package com.carissa.revibes.home_admin.data.remote

import com.carissa.revibes.home_admin.data.model.CreateNewsRequest
import com.carissa.revibes.home_admin.data.model.NewsResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import org.koin.core.annotation.Single

interface NewsRemoteApi {
    @GET("news")
    suspend fun getDailyNews(): NewsResponse

    @POST("news")
    suspend fun createNews(@Body request: CreateNewsRequest): NewsResponse
}

@Single
internal class NewsRemoteApiImpl(ktorfit: Ktorfit) :
    NewsRemoteApi by ktorfit.createNewsRemoteApi()
