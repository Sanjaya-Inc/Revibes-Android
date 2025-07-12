/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.home.R
import com.carissa.revibes.home.presentation.component.HomeFooter
import com.carissa.revibes.home.presentation.navigation.HomeGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<HomeGraph>()
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    viewModel: AboutScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    AboutScreenContent(uiState = state, modifier = modifier)
}

@Composable
private fun AboutScreenContent(
    uiState: AboutScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<AboutScreenUiEvent> = EventReceiver { },
) {
    val navigator = RevibesTheme.navigator
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            CommonHeader(
                title = stringResource(R.string.about_us),
                backgroundDrawRes = R.drawable.bg_about_us,
                searchTextFieldValue = TextFieldValue(),
                onBackClicked = navigator::navigateUp,
                onProfileClicked = { eventReceiver.onEvent(AboutScreenUiEvent.NavigateToProfile) }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            AboutScreenMainContent(uiState)
        }
    }
}

@Composable
private fun AboutScreenMainContent(uiState: AboutScreenUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        ) {
            AboutSection(
                title = stringResource(R.string.our_founder),
                content = stringResource(R.string.our_founder_desc),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = RevibesTheme.colors.primary.copy(alpha = 0.5f)
            )
            AboutSection(
                title = stringResource(R.string.our_goals),
                content = stringResource(R.string.our_goals_desc),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        Text(
            text = stringResource(R.string.our_brand),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Thin,
                fontSize = 20.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        )

        AboutBrandRow(
            imageRes = R.drawable.ic_re,
            imageSize = 72.dp,
            imageAlignment = Alignment.Top,
            imageFirst = true,
            imageContentDescription = stringResource(R.string.re_logo_content_desc),
            text = stringResource(R.string.re_logo_desc),
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        AboutBrandRow(
            imageRes = R.drawable.ic_vibe,
            imageSize = 48.dp,
            imageAlignment = Alignment.CenterVertically,
            imageFirst = false,
            imageContentDescription = stringResource(R.string.vibe_logo_content_desc),
            text = stringResource(R.string.vibe_logo_desc),
            cardColor = Color(0xFF6DC2ED),
            cardShape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        AboutBrandRow(
            imageRes = R.drawable.ic_s,
            imageSize = 48.dp,
            imageAlignment = Alignment.CenterVertically,
            imageFirst = true,
            imageContentDescription = stringResource(R.string.s_logo_content_desc),
            text = stringResource(R.string.s_logo_desc),
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        HomeFooter(uiState.footerItems)
    }
}

@Composable
private fun AboutBrandRow(
    text: String,
    imageRes: Int,
    modifier: Modifier = Modifier,
    imageSize: Dp = 48.dp,
    imageAlignment: Alignment.Vertical = Alignment.CenterVertically,
    imageFirst: Boolean = true,
    imageContentDescription: String? = null,
    cardColor: Color = Color.LightGray,
    cardShape: Shape = RoundedCornerShape(8.dp)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageFirst) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = imageContentDescription,
                modifier = Modifier
                    .size(imageSize)
                    .align(imageAlignment)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            modifier = Modifier.weight(1f),
            shape = cardShape,
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (!imageFirst) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = imageContentDescription,
                modifier = Modifier
                    .size(imageSize)
                    .align(imageAlignment)
            )
        }
    }
}

@Composable
private fun AboutSection(title: String, content: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Thin,
                fontSize = 20.sp
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Left
        )
    }
}

@Composable
@Preview
private fun AboutScreenPreview() {
    RevibesTheme {
        AboutScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = AboutScreenUiState()
        )
    }
}
