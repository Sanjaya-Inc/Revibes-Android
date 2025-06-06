package com.carissa.revibes.core.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle

private const val ANIMATION_DURATION = 300
object RevibesHostNavigationStyle : NavHostAnimatedDestinationStyle() {
    override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
        get() = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(ANIMATION_DURATION)
            ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
        }

    override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
        get() = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(ANIMATION_DURATION)
            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
        }

    override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
        get() = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(ANIMATION_DURATION)
            ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
        }

    override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
        get() = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(ANIMATION_DURATION)
            ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
        }
}
