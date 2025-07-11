package com.carissa.revibes.home.data

import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.data.user.model.UserData
import com.carissa.revibes.home.data.mapper.toUserData
import com.carissa.revibes.home.data.model.HomeBannerData
import com.carissa.revibes.home.data.remote.HomeRemoteApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Single

data class HomeData(
    val banners: List<HomeBannerData>,
    val userData: UserData
)

interface HomeRepository {
    suspend fun getHomeData(): HomeData
}

@Single
internal class HomeRepositoryImpl(
    private val remoteApi: HomeRemoteApi,
    private val userDataSource: UserDataSource
) : HomeRepository {
    override suspend fun getHomeData(): HomeData = coroutineScope {
        val bannersDeferred = async { remoteApi.getBanners().data }
        val userDataDeferred = async {
            val userData = remoteApi.getUserMe().toUserData()
            userDataSource.setUserValue(userData)
            userData
        }

        HomeData(
            banners = bannersDeferred.await(),
            userData = userDataDeferred.await()
        )
    }
}
