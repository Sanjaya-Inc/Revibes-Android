package com.carissa.revibes.core.presentation.compose.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ContentStateSwitcher(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    error: String? = null,
    actionButton: Pair<String, () -> Unit>? = null,
    onContent: @Composable () -> Unit
) {
    AnimatedContent(
        Pair(isLoading, !error.isNullOrBlank()),
        modifier = modifier
    ) { (isLoading, isError) ->
        when {
            isError -> {
                GeneralError(error.orEmpty(), actionButton = actionButton)
            }
            isLoading -> {
                RevibesLoading()
            }
            else -> {
                onContent()
            }
        }
    }
}
