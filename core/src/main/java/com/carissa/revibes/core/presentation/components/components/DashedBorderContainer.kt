package com.carissa.revibes.core.presentation.components.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.modifiers.dashedBorder

@Composable
fun DashedBorderContainer(
    modifier: Modifier = Modifier,
    borderColor: Color = RevibesTheme.colors.primary,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 4.dp,
    cornerRadius: Dp = 8.dp,
    innerPadding: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .dashedBorder(
                    color = borderColor,
                    strokeWidth = strokeWidth,
                    dashLength = dashLength,
                    gapLength = gapLength,
                    cornerRadius = cornerRadius
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .dashedBorder(
                    color = borderColor,
                    strokeWidth = strokeWidth,
                    dashLength = dashLength,
                    gapLength = gapLength,
                    cornerRadius = cornerRadius
                )
                .padding(innerPadding)
        ) {
            content()
        }
    }
}
