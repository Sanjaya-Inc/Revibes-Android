package com.carissa.revibes.presentation.screen.splash

import com.carissa.revibes.core.presentation.BaseViewModel
import kotlinx.coroutines.delay
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Duration.Companion.seconds

data class SplashScreenUiState(val isLoading: Boolean = false)

sealed interface SplashScreenUiEvent {
    data object NavigateToOnboarding : SplashScreenUiEvent
}

@KoinViewModel
class SplashScreenViewModel : BaseViewModel<SplashScreenUiState, SplashScreenUiEvent>(
    SplashScreenUiState(),
    onCreate = {
        delay(1.seconds)
        postSideEffect(SplashScreenUiEvent.NavigateToOnboarding)
    }
)
