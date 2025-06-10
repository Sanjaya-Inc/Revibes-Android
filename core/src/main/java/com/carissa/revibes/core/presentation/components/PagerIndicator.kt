package com.carissa.revibes.core.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
    currentPage: Int,
    totalPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPage) {
            PagerIndicatorItem(isCurrentActive = it == currentPage)
        }
    }
}

@Composable
private fun PagerIndicatorItem(
    isCurrentActive: Boolean,
    modifier: Modifier = Modifier
) {
    val bulletSize = 8.dp
    val orbitPadding = 4.dp
    val activeSize = bulletSize + orbitPadding * 2

    val animatedSize by animateDpAsState(
        targetValue = if (isCurrentActive) activeSize else bulletSize,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "indicatorSize"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isCurrentActive) 1.2f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "indicatorScale"
    )

    Box(
        modifier = modifier
            .size(animatedSize)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .then(
                if (isCurrentActive) {
                    Modifier.border(
                        width = 2.dp,
                        color = RevibesTheme.colors.primary.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(bulletSize)
                .background(RevibesTheme.colors.primary, shape = CircleShape)
        )
    }
}

@Composable
@Preview
private fun PagerIndicatorPreview() {
    RevibesTheme {
        PagerIndicator(currentPage = 1, totalPage = 3, modifier = Modifier.padding(16.dp))
    }
}
