package com.carissa.revibes.core.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle
private const val DURATION_LONG = 500
private const val DURATION_MEDIUM = 400
private const val DURATION_SHORT = 250

object RevibesHostNavigationStyle : NavHostAnimatedDestinationStyle() {

    override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
        get() = {
            // Subtle parallax slide with depth fade+scale
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth / 2 }, // half distance for smoother entry
                animationSpec = tween(
                    durationMillis = DURATION_LONG,
                    easing = EaseOutQuart // cinematic slowdown at the end
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = DURATION_MEDIUM,
                    easing = LinearOutSlowInEasing
                ),
                initialAlpha = 0.05f
            ) + scaleIn(
                animationSpec = tween(
                    durationMillis = DURATION_LONG,
                    easing = EaseOutBack // subtle overshoot gives "premium snap"
                ),
                initialScale = 0.90f
            )
        }

    override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
        get() = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -(fullWidth / 4) }, // slower drift out
                animationSpec = tween(
                    durationMillis = DURATION_MEDIUM,
                    easing = EaseInOutCubic
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = DURATION_SHORT,
                    easing = FastOutLinearInEasing
                )
            ) + scaleOut(
                animationSpec = tween(
                    durationMillis = DURATION_MEDIUM,
                    easing = EaseInOutCubic
                ),
                targetScale = 0.96f
            )
        }

    override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
        get() = {
            // Feels like a "card coming back" from the left
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth / 3 },
                animationSpec = tween(
                    durationMillis = DURATION_LONG,
                    easing = EaseOutQuart
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = DURATION_MEDIUM,
                    easing = LinearOutSlowInEasing
                ),
                initialAlpha = 0.1f
            ) + scaleIn(
                animationSpec = tween(
                    durationMillis = DURATION_MEDIUM,
                    easing = EaseOutBack
                ),
                initialScale = 0.93f
            )
        }

    override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
        get() = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth / 2 }, // half screen exit
                animationSpec = tween(
                    durationMillis = DURATION_LONG,
                    easing = EaseInCubic
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = DURATION_SHORT,
                    easing = FastOutLinearInEasing
                )
            ) + scaleOut(
                animationSpec = tween(
                    durationMillis = DURATION_MEDIUM,
                    easing = EaseInOutCubic
                ),
                targetScale = 1.05f // tiny zoom-up on exit for depth
            )
        }
}
