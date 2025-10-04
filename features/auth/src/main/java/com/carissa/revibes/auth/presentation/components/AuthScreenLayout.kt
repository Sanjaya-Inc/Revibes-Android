/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.auth.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.carissa.revibes.auth.R
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import kotlinx.coroutines.launch

/**
 * A reusable layout for authentication screens (login, register, etc.)
 * Provides common structure with back button, scrollable content, and bottom actions
 */
@Composable
fun AuthScreenLayout(
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: Modifier = Modifier
        .padding(horizontal = 32.dp)
        .padding(top = 32.dp, bottom = 100.dp)
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            AuthBackButton(onClick = onBackClick)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .then(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
            ) {
                bottomContent()
            }

            // Scroll button
            AuthScrollButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                scrollState = scrollState,
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            )
        }
    }
}

/**
 * Back button used in auth screens
 */
@Composable
fun AuthBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.back_cta),
            modifier = Modifier.size(86.dp),
            tint = Color.Unspecified,
            contentDescription = "Back"
        )
    }
}

/**
 * Animated scroll button that appears when content is scrollable
 */
@Composable
fun AuthScrollButton(
    scrollState: androidx.compose.foundation.ScrollState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showScrollButton by remember {
        derivedStateOf {
            scrollState.value < scrollState.maxValue
        }
    }
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    AnimatedVisibility(
        visible = showScrollButton,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = modifier
            .padding(bottom = 150.dp, end = 24.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .offset {
                    IntOffset(0, offsetY.toInt())
                }
                .size(56.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.back_cta),
                modifier = Modifier
                    .size(32.dp)
                    .rotate(270f),
                tint = RevibesTheme.colors.primary,
                contentDescription = "Scroll to Bottom"
            )
        }
    }
}
