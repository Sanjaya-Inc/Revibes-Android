package com.carissa.revibes.auth.domain.usecase

import com.carissa.revibes.auth.data.AuthRepository
import com.carissa.revibes.auth.data.local.DeviceIdDataSource
import com.carissa.revibes.auth.presentation.mapper.toUserData
import com.carissa.revibes.core.data.auth.local.AuthTokenDataSource
import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.data.user.model.UserData
import com.carissa.revibes.core.data.utils.ApiException
import com.carissa.revibes.core.domain.utils.BaseUseCase
import com.carissa.revibes.core.presentation.util.AppDispatchers
import com.carissa.revibes.core.presentation.util.AppScope
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Factory

@Factory
class DoLoginUseCase(
    private val authRepo: AuthRepository,
    private val userDataSource: UserDataSource,
    private val authTokenDataSource: AuthTokenDataSource,
    private val deviceIdDataSource: DeviceIdDataSource,
    private val appDispatchers: AppDispatchers,
    private val appScope: AppScope
) : BaseUseCase() {

    suspend operator fun invoke(email: String, password: String): UserData {
        return execute {
            coroutineScope {
                val userData = async(appDispatchers.io) {
                    val result = authRepo.loginWithEmail(email, password)
                    authTokenDataSource.setAuthToken(result.tokens.accessToken)
                    result.user.toUserData(email).also { userData ->
                        userDataSource.setUserValue(userData)
                    }
                }
                val fcmToken = async(appDispatchers.io) {
                    runCatching {
                        FirebaseMessaging.getInstance().token.await()
                    }.getOrNull()
                }
                val deviceId = deviceIdDataSource.getOrGenerateDeviceId()
                userData.await().also {
                    appScope.async {
                        authRepo.registerDevice(deviceId, fcmToken.await().orEmpty())
                    }
                }
            }
        }
    }

    override fun mapError(e: ApiException): Throwable {
        return when (e.errorResponse?.code) {
            401 -> Throwable("Invalid credentials, please check your email and password")
            else -> super.mapError(e)
        }
    }
}
