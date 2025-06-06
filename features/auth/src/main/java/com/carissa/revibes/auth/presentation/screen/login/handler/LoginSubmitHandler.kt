package com.carissa.revibes.auth.presentation.screen.login.handler

import com.carissa.revibes.auth.data.AuthRepository
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiEvent
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class LoginSubmitHandler(
    private val authRepo: AuthRepository
) {
    suspend fun doLogin(
        syntax: Syntax<LoginScreenUiState, LoginScreenUiEvent>,
    ) {
        syntax.reduce {
            this.state.copy(isLoading = true)
        }
        authRepo.loginWithEmail(syntax.state.email.text.trim(), syntax.state.password.text.trim())
        syntax.reduce {
            this.state.copy(isLoading = false)
        }
    }
}
