package com.carissa.revibes.core.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.carissa.revibes.core.R

@Composable
fun MainBackground(
    modifier: Modifier = Modifier
) {
    AsyncImage(
        R.drawable.main_bg,
        contentDescription = "bg",
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize()
    )
}
