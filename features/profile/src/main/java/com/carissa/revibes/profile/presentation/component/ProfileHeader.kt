package com.carissa.revibes.profile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.core.presentation.compose.components.textfield.OutlinedTextField
import com.carissa.revibes.core.presentation.compose.components.textfield.OutlinedTextFieldDefaults
import com.carissa.revibes.profile.R

@Composable
fun ProfileHeader(
    textFiledValue: TextFieldValue,
    modifier: Modifier = Modifier,
    onTextChange: (TextFieldValue) -> Unit = {},
    onBackClicked: () -> Unit = {}
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
        OutlinedTextField(
            textFiledValue,
            onTextChange,
            modifier = Modifier.weight(1f),
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
    }
}

@Composable
@Preview
private fun ProfileHeaderPreview() {
    RevibesTheme {
        Box(modifier = Modifier.background(Color.White)) {
            ProfileHeader(TextFieldValue())
        }
    }
}
