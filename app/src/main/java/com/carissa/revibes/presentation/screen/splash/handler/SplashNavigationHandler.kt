package com.carissa.revibes.presentation.screen.splash.handler

import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.presentation.screen.splash.SplashScreenUiEvent
import com.carissa.revibes.presentation.screen.splash.SplashScreenUiState
import kotlinx.coroutines.delay
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax
import kotlin.time.Duration.Companion.seconds

@Factory
class SplashNavigationHandler(
    private val userDataSource: UserDataSource
) {
    suspend fun onSplashScreenEnter(
        eventReceiver: EventReceiver<SplashScreenUiEvent>,
        syntax: Syntax<SplashScreenUiState, SplashScreenUiEvent>
    ) = syntax.run {
        delay(1.seconds)
        if (userDataSource.getUserValue().getOrNull() != null
        ) {
            eventReceiver.onEvent(SplashScreenUiEvent.NavigateToHome)
        } else {
            eventReceiver.onEvent(SplashScreenUiEvent.NavigateToOnboarding)
        }
    }
}
