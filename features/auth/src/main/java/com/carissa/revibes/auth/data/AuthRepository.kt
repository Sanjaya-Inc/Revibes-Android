package com.carissa.revibes.auth.data

import com.carissa.revibes.auth.data.remote.AuthRemoteApi
import org.koin.core.annotation.Single

interface AuthRepository {
    suspend fun loginWithEmail(email: String, password: String)
    suspend fun signUpWithEmail(
        email: String,
        displayName: String,
        phoneNumber: String,
        password: String
    )
}

@Single
internal class AuthRepositoryImpl(private val remoteApi: AuthRemoteApi) : AuthRepository {
    override suspend fun loginWithEmail(email: String, password: String) {
        remoteApi.loginWithEmail(email, password)
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
}
