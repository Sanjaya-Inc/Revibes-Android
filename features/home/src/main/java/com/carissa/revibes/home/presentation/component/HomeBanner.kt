/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.home.data.model.HomeBannerData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeBanner(
    banners: ImmutableList<HomeBannerData>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { banners.size }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        if (banners.size != 0) {
            AutoScrollingBanner(
                items = banners,
                modifier = Modifier.matchParentSize()
            ) { data ->
                AsyncImage(
                    model = data.imageUrl,
                    contentDescription = "Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(RevibesTheme.colors.outline)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = RevibesTheme.colors.primary
                )
            }
        }
    }
}

@Composable
@Preview
private fun HomeBannerPreview() {
    RevibesTheme {
        HomeBanner(persistentListOf())
    }
}
