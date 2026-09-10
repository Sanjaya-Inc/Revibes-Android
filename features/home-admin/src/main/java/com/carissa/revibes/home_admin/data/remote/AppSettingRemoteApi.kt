package com.carissa.revibes.home_admin.data.remote

import com.carissa.revibes.home_admin.data.model.AppSettingData
import com.carissa.revibes.home_admin.data.model.AppSettingResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.PUT
import org.koin.core.annotation.Single

interface AppSettingRemoteApi {
    @GET("setting/app")
    suspend fun getAppSetting(): AppSettingResponse

    @PUT("setting/app")
    @Headers("Content-Type: application/json")
    suspend fun updateAppSetting(@Body request: AppSettingData): AppSettingResponse
}

@Single
internal class AppSettingRemoteApiImpl(ktorfit: Ktorfit) :
    AppSettingRemoteApi by ktorfit.createAppSettingRemoteApi()
