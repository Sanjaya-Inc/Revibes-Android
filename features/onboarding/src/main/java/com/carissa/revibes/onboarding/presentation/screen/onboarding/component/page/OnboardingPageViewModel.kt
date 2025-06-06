/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding.presentation.screen.onboarding.component.page

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.onboarding.R
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

data class OnboardingPageUiState(
    @StringRes val text: Int = -1,
    @DrawableRes val image: Int = -1,
    val isShowLoginRegister: Boolean = false
)

sealed interface OnboardingPageUiEvent {
    data object NavigateToRegister : NavigationEvent, OnboardingPageUiEvent
    data object NavigateToLogin : NavigationEvent, OnboardingPageUiEvent
}

@KoinViewModel
class OnboardingPageViewModel(
    @InjectedParam page: Int,
    private val navigationEventBus: NavigationEventBus
) : BaseViewModel<OnboardingPageUiState, OnboardingPageUiEvent>(
    when (page) {
        0 -> R.string.title_onboarding_page_1 to R.drawable.illus_step_1
        1 -> R.string.title_onboarding_page_2 to R.drawable.illus_step_2
        else -> R.string.title_onboarding_page_3 to R.drawable.main_char_both
    }.let { (stringRes, image) ->
        OnboardingPageUiState(
            text = stringRes,
            image = image,
            isShowLoginRegister = page == 2
        )
    }
) {
    override fun onEvent(event: OnboardingPageUiEvent) {
        super.onEvent(event)
        when (event) {
            is OnboardingPageUiEvent.NavigateToLogin -> navigationEventBus.post(event)
            is OnboardingPageUiEvent.NavigateToRegister -> navigationEventBus.post(event)
        }
    }
}
