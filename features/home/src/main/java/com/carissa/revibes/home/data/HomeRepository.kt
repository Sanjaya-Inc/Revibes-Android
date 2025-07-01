package com.carissa.revibes.home.data

import com.carissa.revibes.home.data.model.HomeBannerData
import com.carissa.revibes.home.data.remote.HomeRemoteApi
import org.koin.core.annotation.Single

interface HomeRepository {
    suspend fun getBanners(): List<HomeBannerData>
}

@Single
internal class HomeRepositoryImpl(private val remoteApi: HomeRemoteApi) : HomeRepository {
    override suspend fun getBanners(): List<HomeBannerData> {
        return remoteApi.getBanners().data
    }
}
