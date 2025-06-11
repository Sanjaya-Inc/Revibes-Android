/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.home.data.model.HomeBannerData
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeBanner(
    banners: PersistentList<HomeBannerData>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { banners.size }
    Box(
        modifier = modifier.fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        AutoScrollingBanner(
            items = banners,
            modifier = Modifier.matchParentSize()
        ) { data ->
            // This is the content for each banner page
            AsyncImage(
                model = data.imageUrl,
                contentDescription = data.deeplink,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
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
