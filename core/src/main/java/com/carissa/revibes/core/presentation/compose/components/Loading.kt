package com.carissa.revibes.core.presentation.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.carissa.revibes.core.presentation.compose.RevibesTheme

@Composable
fun RevibesLoading(modifier: Modifier = Modifier, color: Color = RevibesTheme.colors.primary) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = RevibesTheme.colors.primary
        )
    }
}
