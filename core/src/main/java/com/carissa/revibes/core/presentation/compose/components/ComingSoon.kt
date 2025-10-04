package com.carissa.revibes.core.presentation.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.compose.RevibesTheme

@Composable
fun ComingSoon(
    featureName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(R.drawable.ic_maintenance_girl_illustration),
            contentDescription = stringResource(R.string.coming_soon_illustration_desc),
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.coming_soon_title),
            style = RevibesTheme.typography.h1,
            color = RevibesTheme.colors.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.coming_soon_message, featureName),
            style = RevibesTheme.typography.body1,
            color = RevibesTheme.colors.text,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            variant = ButtonVariant.Primary,
            text = stringResource(R.string.back_to_home),
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
