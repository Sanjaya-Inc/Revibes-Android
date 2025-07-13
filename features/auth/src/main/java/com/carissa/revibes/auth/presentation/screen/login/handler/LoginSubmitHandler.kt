package com.carissa.revibes.auth.presentation.screen.login.handler

import com.carissa.revibes.auth.data.AuthRepository
import com.carissa.revibes.auth.data.local.DeviceIdDataSource
import com.carissa.revibes.auth.presentation.mapper.toUserData
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiEvent
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiState
import com.carissa.revibes.core.data.auth.local.AuthTokenDataSource
import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.presentation.EventReceiver
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class LoginSubmitHandler(
    private val authRepo: AuthRepository,
    private val userDataSource: UserDataSource,
    private val authTokenDataSource: AuthTokenDataSource,
    private val deviceIdDataSource: DeviceIdDataSource,
) {
    suspend fun doLogin(
        eventReceiver: EventReceiver<LoginScreenUiEvent>,
        syntax: Syntax<LoginScreenUiState, LoginScreenUiEvent>,
    ) {
        syntax.reduce {
            this.state.copy(isLoading = true)
        }

        val email = syntax.state.email.text.trim()
        val password = syntax.state.password.text.trim()

        val loginResult = authRepo.loginWithEmail(email, password)

        syntax.reduce {
            this.state.copy(isLoading = false)
        }

        authTokenDataSource.setAuthToken(loginResult.tokens.accessToken)
        val userData = loginResult.user.toUserData(email)
        userDataSource.setUserValue(userData)

        val deviceId = deviceIdDataSource.getOrGenerateDeviceId()
        val fcmToken = FirebaseMessaging.getInstance().token.await()

        authRepo.registerDevice(deviceId, fcmToken)

        eventReceiver.onEvent(LoginScreenUiEvent.NavigateToHome)
    }
}
