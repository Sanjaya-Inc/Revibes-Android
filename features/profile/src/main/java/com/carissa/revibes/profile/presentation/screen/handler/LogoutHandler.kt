package com.carissa.revibes.profile.presentation.screen.handler

import com.carissa.revibes.core.data.auth.local.AuthTokenDataSource
import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.profile.presentation.screen.ProfileScreenUiEvent
import com.carissa.revibes.profile.presentation.screen.ProfileScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class LogoutHandler(
    private val userDataSource: UserDataSource,
    private val authTokenDataSource: AuthTokenDataSource
) {
    fun onLogout(
        eventReceiver: EventReceiver<ProfileScreenUiEvent>,
        syntax: Syntax<ProfileScreenUiState, ProfileScreenUiEvent>
    ) = syntax.run {
        authTokenDataSource.clearAuthToken()
        userDataSource.clearUserData()
        eventReceiver.onEvent(ProfileScreenUiEvent.NavigateToLogin)
    }
}
