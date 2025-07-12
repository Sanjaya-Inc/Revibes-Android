package com.carissa.revibes.core.presentation.components.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme

@Composable
fun GeneralError(message: String, modifier: Modifier = Modifier, onRetry: (() -> Unit)? = null) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = message, style = RevibesTheme.typography.h3, textAlign = TextAlign.Center)
        AnimatedVisibility(onRetry != null) {
            Button(onClick = onRetry!!) { Text(text = "Retry") }
        }
    }
}
