package com.carissa.revibes.auth.data

import com.carissa.revibes.auth.data.mapper.toDomain
import com.carissa.revibes.auth.data.model.DeviceRegistrationRequest
import com.carissa.revibes.auth.data.remote.AuthRemoteApi
import com.carissa.revibes.auth.domain.model.LoginResult
import org.koin.core.annotation.Single

interface AuthRepository {
    suspend fun loginWithEmail(email: String, password: String): LoginResult
    suspend fun signUpWithEmail(
        email: String,
        displayName: String,
        phoneNumber: String,
        password: String
    )

    suspend fun registerDevice(deviceToken: String, fcmToken: String)
}

@Single
internal class AuthRepositoryImpl(private val remoteApi: AuthRemoteApi) : AuthRepository {
    override suspend fun loginWithEmail(email: String, password: String): LoginResult {
        return remoteApi.loginWithEmail(email, password).toDomain()
    }

    override suspend fun signUpWithEmail(
        email: String,
        displayName: String,
        phoneNumber: String,
        password: String
    ) {
        remoteApi.signUpWithEmail(
            email = email,
            displayName = displayName,
            phoneNumber = phoneNumber,
            password = password
        )
    }

    override suspend fun registerDevice(deviceToken: String, fcmToken: String) {
        remoteApi.registerDevice(
            request = DeviceRegistrationRequest(
                deviceToken = deviceToken,
                fcmToken = fcmToken
            )
        )
    }
}
