/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.shop.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.core.presentation.util.DeeplinkHandler
import com.carissa.revibes.shop.R
import com.carissa.revibes.shop.presentation.navigation.ShopGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Destination<ShopGraph>(start = true)
@Composable
fun ShopScreen(
    modifier: Modifier = Modifier,
    viewModel: ShopScreenViewModel = koinViewModel()
) {
    ShopScreenContent(modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun ShopScreenContent(
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ShopScreenUiEvent> = EventReceiver { }
) {
    val navigator = RevibesTheme.navigator
    val deeplinkHandler: DeeplinkHandler = koinInject()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CommonHeader(
            title = stringResource(R.string.shop_title),
            searchTextFieldValue = TextFieldValue(),
            backgroundDrawRes = R.drawable.bg_shop_header,
            onBackClicked = navigator::navigateUp,
            onProfileClicked = { eventReceiver.onEvent(ShopScreenUiEvent.NavigateToProfile) }
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.shop_tap_to_access),
                style = RevibesTheme.typography.body1,
                color = RevibesTheme.colors.primary,
                modifier = Modifier.padding(top = 18.dp)
            )
            ShopItemCard(
                modifier = Modifier.padding(top = 18.dp),
                imageRes = R.drawable.logo_shopee,
                imageModifier = Modifier.padding(10.dp),
                onItemClicked = { deeplinkHandler.openUrl("https://shopee.co.id/nbams_dr1o") }
            )

            ShopItemCard(
                modifier = Modifier.padding(top = 16.dp),
                imageRes = R.drawable.logo_tokopedia,
                onItemClicked = { deeplinkHandler.openUrl("https://www.tokopedia.com/revibes") }
            )

            ShopItemCard(
                modifier = Modifier.padding(top = 16.dp),
                imageRes = R.drawable.ic_add,
                imageModifier = Modifier.size(36.dp),
                text = stringResource(R.string.soon)
            )
        }
    }
}

@Composable
private fun ShopItemCard(
    imageRes: Int,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    contentDescription: String? = null,
    text: String? = null,
    textStyle: androidx.compose.ui.text.TextStyle = RevibesTheme.typography.body1,
    textColor: Color = RevibesTheme.colors.primary,
    onItemClicked: () -> Unit = { }
) {
    val baseModifier = modifier
        .fillMaxWidth()
        .height(64.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color("#E0F1E1".toColorInt()))

    if (text == null) {
        Box(
            modifier = baseModifier.clickable(onClick = onItemClicked),
        ) {
            AsyncImage(
                model = imageRes,
                contentDescription = contentDescription,
                modifier = imageModifier.align(Alignment.Center)
            )
        }
    } else {
        Column(
            modifier = baseModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = imageRes,
                contentDescription = contentDescription,
                modifier = imageModifier
            )
            Text(
                text = text,
                style = textStyle,
                color = textColor
            )
        }
    }
}

@Composable
@Preview
private fun ShopScreenPreview() {
    RevibesTheme {
        ShopScreenContent(
            modifier = Modifier.background(Color.White),
        )
    }
}
