package com.carissa.revibes.core.presentation.compose.components

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
    val targetState = when {
        !error.isNullOrBlank() -> "ERROR"
        isLoading -> "LOADING"
        else -> "CONTENT"
    }
    StateSwitcherAnimator(targetState, modifier) { state ->
        when (state) {
            "ERROR" -> GeneralError(error.orEmpty(), actionButton = actionButton)
            "LOADING" -> RevibesLoading()
            "CONTENT" -> onContent()
        }
    }
}
