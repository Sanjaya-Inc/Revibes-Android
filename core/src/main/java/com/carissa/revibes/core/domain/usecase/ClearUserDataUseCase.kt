package com.carissa.revibes.core.domain.usecase

import com.carissa.revibes.core.data.auth.local.AuthTokenDataSource
import com.carissa.revibes.core.data.user.local.UserDataSource
import org.koin.core.annotation.Single

@Single
class ClearUserDataUseCase(
    private val userDataSource: UserDataSource,
    private val authTokenDataSource: AuthTokenDataSource
) {
    operator fun invoke() {
        userDataSource.clearUserData()
        authTokenDataSource.clearAuthToken()
    }
}
