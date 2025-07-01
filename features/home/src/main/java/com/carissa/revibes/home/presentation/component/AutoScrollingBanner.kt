/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home.presentation.component
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.PagerIndicator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val AUTO_SCROLL_DELAY_MS = 3000L // 3 seconds

@Composable
fun <T> AutoScrollingBanner(
    items: ImmutableList<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (item: T) -> Unit
) {
    // 1. A very large number for the page count to simulate infinity.
    val pageCount = Int.MAX_VALUE
    val actualPageCount = items.size

    // 2. Start at a page that is a multiple of the actual item count.
    // This allows scrolling in both directions from the start.
    val startIndex = pageCount / 2
    val pagerState = rememberPagerState(initialPage = startIndex) { pageCount }
    val coroutineScope = rememberCoroutineScope()

    // 3. State to track if the user is currently dragging the pager.
    var isUserDragging by remember { mutableStateOf(false) }

    // 4. LaunchedEffect to handle the auto-scrolling.
    LaunchedEffect(key1 = isUserDragging) {
        if (!isUserDragging) {
            while (true) {
                delay(AUTO_SCROLL_DELAY_MS)
                // Animate to the next page.
                val nextPage = pagerState.currentPage + 1
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .matchParentSize()
                // 5. Detect user drag gestures to pause auto-scrolling.
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { isUserDragging = true },
                        onDragEnd = {
                            // Resume auto-scrolling when dragging ends.
                            isUserDragging = false
                            // Optional: Snap to the nearest page after drag.
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage)
                            }
                        },
                        onDragCancel = { isUserDragging = false }
                    ) { change, _ -> }
                }
        ) { page ->
            // 6. Use the modulo operator to map the "infinite" page index
            // to a valid index in our items list.
            val actualIndex = (page - startIndex).mod(actualPageCount)
            itemContent(items[actualIndex])
        }

        // Optional: Add a pager indicator.
        PagerIndicator(
            currentPage = pagerState.currentPage.mod(actualPageCount),
            totalPage = actualPageCount,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
}
