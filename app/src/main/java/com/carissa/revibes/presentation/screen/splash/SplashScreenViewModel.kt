package com.carissa.revibes.presentation.screen.splash

import com.carissa.revibes.core.presentation.BaseViewModel
import org.koin.android.annotation.KoinViewModel

data class SplashScreenUiState(val isLoading: Boolean = false)

sealed interface SplashScreenUiEvent

@KoinViewModel
class SplashScreenViewModel :
    BaseViewModel<SplashScreenUiState, SplashScreenUiEvent>(SplashScreenUiState())
