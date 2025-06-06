package com.carissa.revibes.core.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle

private const val ANIMATION_DURATION_MAIN = 450
private const val ANIMATION_DURATION_FADE = 380
private const val ANIMATION_DURATION_SCALE = 500

object RevibesHostNavigationStyle : NavHostAnimatedDestinationStyle() {
    override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
        get() = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_FADE,
                    easing = FastOutSlowInEasing
                ),
                initialAlpha = 0.2f
            ) + scaleIn(
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_SCALE,
                    easing = EaseInOutCubic
                ),
                initialScale = 0.95f
            )
        }

    override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
        get() = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth / 3 },
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_MAIN,
                    easing = EaseInOutBack
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_FADE - 50,
                    easing = FastOutSlowInEasing
                )
            ) + scaleOut(
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_SCALE - 100,
                    easing = EaseInOutCubic
                ),
                targetScale = 0.92f
            )
        }

    override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
        get() = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth / 2 },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_FADE - 80,
                    easing = FastOutSlowInEasing
                ),
                initialAlpha = 0.3f
            ) + scaleIn(
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_SCALE - 50,
                    easing = EaseInOutCubic
                ),
                initialScale = 0.97f
            )
        }

    override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
        get() = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_FADE - 30,
                    easing = FastOutSlowInEasing
                )
            ) + scaleOut(
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_SCALE - 80,
                    easing = EaseInOutCubic
                ),
                targetScale = 1.03f
            )
        }
}
