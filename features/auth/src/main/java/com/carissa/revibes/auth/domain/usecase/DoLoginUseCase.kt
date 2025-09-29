package com.carissa.revibes.auth.domain.usecase

import com.carissa.revibes.auth.data.AuthRepository
import com.carissa.revibes.auth.data.local.DeviceIdDataSource
import com.carissa.revibes.auth.presentation.mapper.toUserData
import com.carissa.revibes.core.data.auth.local.AuthTokenDataSource
import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.data.user.model.UserData
import com.carissa.revibes.core.presentation.util.AppDispatcherScope
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
    private val appDispatcherScope: AppDispatcherScope
) {
    suspend operator fun invoke(email: String, password: String): UserData {
        return coroutineScope {
            val userData = async(appDispatcherScope.io) {
                val result = authRepo.loginWithEmail(email, password)
                authTokenDataSource.setAuthToken(result.tokens.accessToken)
                result.user.toUserData(email).also { userData ->
                    userDataSource.setUserValue(userData)
                }
            }
            val fcmToken = async(appDispatcherScope.io) {
                runCatching {
                    FirebaseMessaging.getInstance().token.await()
                }.getOrNull()
            }
            val deviceId = deviceIdDataSource.getOrGenerateDeviceId()
            userData.await().also {
                appDispatcherScope.async {
                    authRepo.registerDevice(deviceId, fcmToken.await().orEmpty())
                }
            }
        }
    }
}
