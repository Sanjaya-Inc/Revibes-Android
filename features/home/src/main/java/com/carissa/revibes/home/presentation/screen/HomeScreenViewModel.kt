/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.home.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.home.presentation.component.FooterItem
import kotlinx.collections.immutable.ImmutableList
import org.koin.android.annotation.KoinViewModel

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue(),
    val footerItems: ImmutableList<FooterItem> = FooterItem.default()
)

sealed interface HomeScreenUiEvent

@KoinViewModel
class HomeScreenViewModel :
    BaseViewModel<HomeScreenUiState, HomeScreenUiEvent>(HomeScreenUiState())
