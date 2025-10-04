package com.carissa.revibes.home.domain

import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.data.user.model.UserData
import com.carissa.revibes.core.domain.usecase.UpdateUserDataUseCase
import com.carissa.revibes.home.data.mapper.toUserData
import com.carissa.revibes.home.data.remote.HomeRemoteApi
import org.koin.core.annotation.Single

@Single
class UpdateUserDataUseCaseImpl(
    private val remoteApi: HomeRemoteApi,
    private val userDataSource: UserDataSource
) : UpdateUserDataUseCase {
    override suspend fun getAndUpdate(): UserData {
        return remoteApi.getUserMe().toUserData().also(userDataSource::setUserValue)
    }
}
