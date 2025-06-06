package com.carissa.revibes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.presentation.navigation.handler.NavigationEventHandler
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.koinInject
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.parameter.parametersOf

@Factory
class NavigationEventHandlerRegistry(
    @InjectedParam navigator: DestinationsNavigator
) {
    private val handlers = listOf<NavigationEventHandler>()

    fun handle(event: NavigationEvent) {
        handlers.firstOrNull { it.canHandle(event) }?.navigate(event)
    }
}

@Composable
fun NavigationEventBusHandler(
    navigator: DestinationsNavigator = RevibesTheme.navigator,
    eventBus: NavigationEventBus = koinInject(),
    navigationEventHandlerRegistry: NavigationEventHandlerRegistry = koinInject {
        parametersOf(navigator)
    }
) {
    LaunchedEffect(Unit) {
        eventBus.collect(navigationEventHandlerRegistry::handle)
    }
}
