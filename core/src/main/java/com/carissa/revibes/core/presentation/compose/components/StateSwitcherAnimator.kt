package com.carissa.revibes.core.presentation.compose.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> StateSwitcherAnimator(
    targetState: T,
    modifier: Modifier = Modifier,
    transitionType: AnimationTransitionType = AnimationTransitionType.Default,
    content: @Composable (T) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            when (transitionType.resolve(targetState)) {
                AnimationTransitionType.Error -> {
                    slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    ) + fadeIn() togetherWith
                        slideOutVertically(
                            targetOffsetY = { -it },
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + fadeOut()
                }
                AnimationTransitionType.Loading -> {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(400)) togetherWith
                        scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) + fadeOut()
                }
                else -> {
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    ) + fadeIn() togetherWith
                        slideOutVertically(
                            targetOffsetY = { it / 2 },
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + fadeOut()
                }
            }
        },
        label = "PremiumAnimatedContent"
    ) { state ->
        content(state)
    }
}

enum class AnimationTransitionType {
    Error,
    Loading,
    Content,
    Default;

    fun <T> resolve(targetState: T): AnimationTransitionType {
        return when (targetState) {
            is String -> when (targetState) {
                "ERROR" -> Error
                "LOADING" -> Loading
                "CONTENT" -> Content
                else -> Default
            }
            is Boolean -> if (targetState) Content else Error
            else -> Default
        }
    }
}
