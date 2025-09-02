/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.home.presentation.screen

import com.carissa.revibes.core.domain.supportdata.GetSupportDataUseCase
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home.presentation.component.FooterItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.android.annotation.KoinViewModel

data class AboutScreenUiState(
    val isLoading: Boolean = false,
    val footerItems: ImmutableList<FooterItem> = persistentListOf()
)

sealed interface AboutScreenUiEvent {
    data object NavigateToProfile : AboutScreenUiEvent, NavigationEvent
}

@KoinViewModel
class AboutScreenViewModel(
    getSupportDataUseCase: GetSupportDataUseCase
) : BaseViewModel<AboutScreenUiState, AboutScreenUiEvent>(
    AboutScreenUiState(
        footerItems = FooterItem.default(getSupportDataUseCase.getSupportData())
    )
)
