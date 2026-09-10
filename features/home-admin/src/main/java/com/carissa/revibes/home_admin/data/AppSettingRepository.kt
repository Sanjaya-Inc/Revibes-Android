package com.carissa.revibes.home_admin.data

import com.carissa.revibes.core.data.utils.BaseRepository
import com.carissa.revibes.home_admin.data.model.AppSettingData
import com.carissa.revibes.home_admin.data.remote.AppSettingRemoteApi
import org.koin.core.annotation.Single

@Single
class AppSettingRepository(
    private val remoteApi: AppSettingRemoteApi
) : BaseRepository() {

    suspend fun getAppSetting(): AppSettingData {
        return execute { remoteApi.getAppSetting().data ?: AppSettingData() }
    }

    suspend fun updateAppSetting(setting: AppSettingData) {
        execute { remoteApi.updateAppSetting(setting) }
    }
}
