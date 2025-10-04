package com.carissa.revibes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.presentation.navigation.handler.NavigationEventHandler
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.koinInject
import org.koin.core.annotation.Factory

@Factory
class NavigationEventHandlerRegistry(
    private val handlers: List<NavigationEventHandler>
) {
    fun handle(navigator: DestinationsNavigator, event: NavigationEvent) {
        handlers.firstOrNull { it.canHandle(event) }?.navigate(navigator, event)
    }
}

@Composable
fun NavigationEventBusHandler(
    navigator: DestinationsNavigator = RevibesTheme.navigator,
    eventBus: NavigationEventBus = koinInject(),
    navigationEventHandlerRegistry: NavigationEventHandlerRegistry = koinInject()
) {
    LaunchedEffect(Unit) {
        eventBus.collect {
            navigationEventHandlerRegistry.handle(navigator, it)
        }
    }
}
