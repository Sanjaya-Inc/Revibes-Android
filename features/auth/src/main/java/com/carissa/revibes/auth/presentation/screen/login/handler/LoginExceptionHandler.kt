package com.carissa.revibes.auth.presentation.screen.login.handler

import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiEvent
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiState
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class LoginExceptionHandler(
    private val navigationEventBus: NavigationEventBus
) {
    suspend fun onLoginError(
        syntax: Syntax<LoginScreenUiState, LoginScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }
        postSideEffect(LoginScreenUiEvent.LoginError(throwable.message.orEmpty()))
        // TODO: Remove this test navigation
        navigationEventBus.post(LoginScreenUiEvent.NavigateToHome)
    }
}
