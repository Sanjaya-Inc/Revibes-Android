package com.carissa.revibes.auth.presentation.screen.login.handler

import android.util.Log
import com.carissa.revibes.auth.data.AuthRepository
import com.carissa.revibes.auth.presentation.mapper.toUserData
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiEvent
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiState
import com.carissa.revibes.core.data.auth.local.AuthTokenDataSource
import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class LoginSubmitHandler(
    private val authRepo: AuthRepository,
    private val navigationEventBus: NavigationEventBus,
    private val userDataSource: UserDataSource,
    private val authTokenDataSource: AuthTokenDataSource,
) {
    suspend fun doLogin(
        syntax: Syntax<LoginScreenUiState, LoginScreenUiEvent>,
    ) {
        syntax.reduce {
            this.state.copy(isLoading = true)
        }

        val email = syntax.state.email.text.trim()
        val password = syntax.state.password.text.trim()

        val loginResult = authRepo.loginWithEmail(email, password).also {
            Log.d("ketai", "doLogin: $it")
        }

        syntax.reduce {
            this.state.copy(isLoading = false)
        }

        authTokenDataSource.setAuthToken(loginResult.tokens.accessToken)
        val userData = loginResult.user.toUserData(email)
        userDataSource.setUserValue(userData)

        navigationEventBus.post(LoginScreenUiEvent.NavigateToHome)
    }
}
