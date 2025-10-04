package com.carissa.revibes.auth.data

import com.carissa.revibes.auth.data.mapper.toDomain
import com.carissa.revibes.auth.data.model.DeviceRegistrationRequest
import com.carissa.revibes.auth.data.remote.AuthRemoteApi
import com.carissa.revibes.auth.domain.model.LoginResult
import com.carissa.revibes.core.data.utils.BaseRepository
import org.koin.core.annotation.Single

@Single
class AuthRepository(private val remoteApi: AuthRemoteApi) : BaseRepository() {

    suspend fun loginWithEmail(email: String, password: String): LoginResult {
        return execute { remoteApi.loginWithEmail(email, password).toDomain() }
    }

    suspend fun signUpWithEmail(
        email: String,
        displayName: String,
        phoneNumber: String,
        password: String
    ) {
        execute {
            remoteApi.signUpWithEmail(
                email = email,
                displayName = displayName,
                phoneNumber = phoneNumber,
                password = password
            )
        }
    }

    suspend fun registerDevice(deviceToken: String, fcmToken: String) {
        execute {
            remoteApi.registerDevice(
                request = DeviceRegistrationRequest(
                    deviceToken = deviceToken,
                    fcmToken = fcmToken
                )
            )
        }
    }
}
