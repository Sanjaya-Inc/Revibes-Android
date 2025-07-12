package com.carissa.revibes.presentation.screen.splash

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.presentation.screen.splash.handler.SplashNavigationHandler
import org.koin.android.annotation.KoinViewModel

data class SplashScreenUiState(val isLoading: Boolean = false)

sealed interface SplashScreenUiEvent {
    data object NavigateToOnboarding : SplashScreenUiEvent, NavigationEvent
    data object NavigateToHome : SplashScreenUiEvent, NavigationEvent
}

@KoinViewModel
class SplashScreenViewModel(
    private val navigationHandler: SplashNavigationHandler
) : BaseViewModel<SplashScreenUiState, SplashScreenUiEvent>(
    SplashScreenUiState(),
    onCreate = { navigationHandler.onSplashScreenEnter(this, it) }
)
