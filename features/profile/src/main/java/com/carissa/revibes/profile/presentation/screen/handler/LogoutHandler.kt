package com.carissa.revibes.profile.presentation.screen.handler

import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.profile.presentation.screen.ProfileScreenUiEvent
import com.carissa.revibes.profile.presentation.screen.ProfileScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class LogoutHandler(
    private val userDataSource: UserDataSource,
    private val navigationEventBus: NavigationEventBus
) {
    fun onLogout(
        syntax: Syntax<ProfileScreenUiState, ProfileScreenUiEvent>
    ) = syntax.run {
        userDataSource.clearUserData()
        navigationEventBus.post(ProfileScreenUiEvent.LogoutClicked)
    }
}
