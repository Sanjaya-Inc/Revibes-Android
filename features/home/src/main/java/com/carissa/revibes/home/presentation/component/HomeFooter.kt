package com.carissa.revibes.home.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.domain.model.SupportData
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.ButtonVariant
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.core.presentation.util.openSupportWhatsApp
import com.carissa.revibes.home.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
@Keep
data class FooterItem(
    @param:DrawableRes val icon: Int,
    val value: String,
    val color: Color? = null
) {
    companion object {
        fun default(supportData: SupportData) = persistentListOf(
            FooterItem(R.drawable.ic_location, "Jakarta, Indonesia", Color.Red),
            FooterItem(R.drawable.ic_phone, "${supportData.phoneNumber} (Hotline)"),
            FooterItem(R.drawable.ic_fax, supportData.faxNumber),
            FooterItem(R.drawable.ic_whatsapp, supportData.whatsappNumber),
            FooterItem(R.drawable.ic_email, supportData.supportEmail),
        )
    }
}

@Composable
fun HomeFooter(
    items: ImmutableList<FooterItem>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        colors = CardColors(
            containerColor = RevibesTheme.colors.primary,
            contentColor = RevibesTheme.colors.onPrimary,
            disabledContainerColor = RevibesTheme.colors.primary,
            disabledContentColor = RevibesTheme.colors.onPrimary
        )
    ) {
        LazyVerticalGrid(
            GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    stringResource(R.string.label_contact_us),
                    color = RevibesTheme.colors.onPrimary,
                    style = RevibesTheme.typography.h2
                )
            }
            items(items) { item ->
                HomeFooterItem(item)
            }
            item {
                val context = LocalContext.current
                Button(
                    variant = ButtonVariant.SecondaryOutlined,
                    onClick = {
                        context.openSupportWhatsApp()
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_chat),
                            contentDescription = null,
                            tint = RevibesTheme.colors.secondary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            stringResource(R.string.cta_chat_team_revibes),
                            style = RevibesTheme.typography.button,
                            color = RevibesTheme.colors.secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun HomeFooterPreview() {
    RevibesTheme {
        Box(modifier = Modifier.background(Color.White)) {
            HomeFooter(
                FooterItem.default(
                    SupportData(
                        supportEmail = "james.s.sherman@example-pet-store.com",
                        phoneNumber = "+62 816 1805 621",
                        whatsappNumber = "+62 816 1805 621",
                        faxNumber = "+62 816 1805 621"
                    )
                )
            )
        }
    }
}
