package com.carissa.revibes.core.presentation.components.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.textfield.OutlinedTextField
import com.carissa.revibes.core.presentation.components.components.textfield.OutlinedTextFieldDefaults
import org.koin.androidx.compose.koinViewModel

@Composable
fun CommonHeader(
    title: String,
    @DrawableRes backgroundDrawRes: Int,
    searchTextFieldValue: TextFieldValue,
    modifier: Modifier = Modifier,
    viewModel: CommonHeaderViewModel = koinViewModel(),
    eventReceiver: EventReceiver<ToolbarEvent> = viewModel,
    subtitle: String? = null,
    onTextChange: (TextFieldValue) -> Unit = {},
    onBackClicked: () -> Unit = { eventReceiver.onEvent(ToolbarEvent.NavigateBack) },
    onProfileClicked: () -> Unit = { eventReceiver.onEvent(ToolbarEvent.NavigateToProfile) },
) {
    val headerHeight = if (subtitle != null) 312.dp else 272.dp
    Box(modifier = modifier.height(headerHeight)) {
        Box {
            AsyncImage(
                model = backgroundDrawRes,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            )
            CommonToolbar(
                searchTextFieldValue = searchTextFieldValue,
                onTextChange = onTextChange,
                onBackClicked = onBackClicked,
                onProfileClicked = onProfileClicked,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (subtitle != null) 76.dp else 36.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(32.dp))
                .background(RevibesTheme.colors.primary),
            contentAlignment = Alignment.Center,
        ) {
            if (subtitle != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = title,
                        style = RevibesTheme.typography.h1,
                        color = RevibesTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = RevibesTheme.colors.onPrimary.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = RevibesTheme.typography.body2,
                        color = RevibesTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            } else {
                Text(
                    text = title,
                    style = RevibesTheme.typography.h1,
                    color = RevibesTheme.colors.onPrimary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun CommonToolbar(
    searchTextFieldValue: TextFieldValue,
    modifier: Modifier = Modifier,
    onTextChange: (TextFieldValue) -> Unit = {},
    onBackClicked: () -> Unit = {},
    onProfileClicked: () -> Unit = {},
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClicked,
        ) {
            Icon(
                painter = painterResource(R.drawable.back_cta),
                modifier = Modifier.size(86.dp),
                tint = Color.Unspecified,
                contentDescription = "Back"
            )
        }
        Row {
            OutlinedTextField(
                value = searchTextFieldValue,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .alpha(0.5f),
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(8.dp),
                        tint = RevibesTheme.colors.onPrimary
                    )
                },
                placeholder = {
                    Text(
                        text = "Search",
                        style = RevibesTheme.typography.input,
                        color = RevibesTheme.colors.onPrimary
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedLeadingIconColor = RevibesTheme.colors.onPrimary,
                    unfocusedLeadingIconColor = RevibesTheme.colors.onPrimary,
                    disabledLeadingIconColor = RevibesTheme.colors.onPrimary,
                    errorLeadingIconColor = RevibesTheme.colors.onPrimary,
                    focusedTextColor = RevibesTheme.colors.onPrimary,
                    unfocusedTextColor = RevibesTheme.colors.onPrimary,
                    disabledTextColor = RevibesTheme.colors.onPrimary,
                    errorTextColor = RevibesTheme.colors.onPrimary,
                    focusedContainerColor = RevibesTheme.colors.primary,
                    unfocusedContainerColor = RevibesTheme.colors.primary,
                    disabledContainerColor = RevibesTheme.colors.primary,
                    errorContainerColor = RevibesTheme.colors.primary,
                    focusedOutlineColor = RevibesTheme.colors.primary,
                    unfocusedOutlineColor = RevibesTheme.colors.primary,
                    disabledOutlineColor = RevibesTheme.colors.primary,
                    errorOutlineColor = RevibesTheme.colors.primary,
                )
            )
            Spacer(modifier = Modifier.size(16.dp))
            IconButton(
                onClick = onProfileClicked,
                colors = IconButtonColors(
                    containerColor = RevibesTheme.colors.primary,
                    contentColor = RevibesTheme.colors.onPrimary,
                    disabledContainerColor = RevibesTheme.colors.primary,
                    disabledContentColor = RevibesTheme.colors.onPrimary
                )
            ) {
                Icon(
                    painterResource(R.drawable.ic_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(8.dp),
                    tint = RevibesTheme.colors.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommonHeaderPreview() {
    RevibesTheme {
        CommonHeader(
            title = "Common Header",
            backgroundDrawRes = -1,
            searchTextFieldValue = TextFieldValue(""),
            onTextChange = {},
            modifier = Modifier.background(Color.White)
        )
    }
}

@Preview
@Composable
private fun CommonHeaderWithSubtitlePreview() {
    RevibesTheme {
        CommonHeader(
            title = "Common Header",
            subtitle = "This is a subtitle",
            backgroundDrawRes = -1,
            searchTextFieldValue = TextFieldValue(""),
            onTextChange = {},
            modifier = Modifier.background(Color.White)
        )
    }
}
