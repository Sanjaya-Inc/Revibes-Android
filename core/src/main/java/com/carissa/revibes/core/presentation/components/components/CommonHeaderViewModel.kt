package com.carissa.revibes.core.presentation.components.components

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import org.koin.android.annotation.KoinViewModel

sealed interface ToolbarEvent {
    object NavigateToProfile : NavigationEvent, ToolbarEvent
    object NavigateBack : NavigationEvent, ToolbarEvent
}

@KoinViewModel
class CommonHeaderViewModel : BaseViewModel<Unit, ToolbarEvent>(Unit)
