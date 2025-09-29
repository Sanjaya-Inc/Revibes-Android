package com.carissa.revibes.auth.presentation.screen.login.handler

import com.carissa.revibes.auth.domain.usecase.DoLoginUseCase
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiEvent
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiState
import com.carissa.revibes.core.presentation.EventReceiver
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class LoginSubmitHandler(
    private val doLoginUseCase: DoLoginUseCase
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
        val userData = doLoginUseCase(email, password)
        if (userData.role == "admin") {
            eventReceiver.onEvent(LoginScreenUiEvent.NavigateToAdminHome)
        } else {
            eventReceiver.onEvent(LoginScreenUiEvent.NavigateToHome)
        }

        syntax.reduce {
            this.state.copy(isLoading = false)
        }
    }
}
