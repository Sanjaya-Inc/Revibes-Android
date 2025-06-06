package com.carissa.revibes.core.presentation.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.carissa.revibes.core.presentation.components.foundation.ripple
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator

object RevibesTheme {
    val colors: Colors
        @ReadOnlyComposable @Composable
        get() = LocalColors.current

    val typography: Typography
        @ReadOnlyComposable @Composable
        get() = LocalTypography.current

    val navigator: DestinationsNavigator
        @Composable
        @ReadOnlyComposable
        get() = LocalRevibesNavigator.current
}

@Composable
fun RevibesTheme(
    // isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable (NavHostController) -> Unit
) {
    val rippleIndication = ripple()
    val selectionColors = rememberTextSelectionColors(LightColors)
    val typography = provideTypography()
    val colors =
        /** if (isDarkTheme) DarkColors else **/
        LightColors
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        LocalIndication provides rippleIndication,
        LocalTextSelectionColors provides selectionColors,
        LocalContentColor provides colors.contentColorFor(colors.background),
        LocalTextStyle provides typography.body1,
        LocalRevibesNavigator provides navController.rememberDestinationsNavigator(),
        content = { content(navController) },
    )
}

@Composable
fun contentColorFor(color: Color): Color {
    return RevibesTheme.colors.contentColorFor(color)
}

@Composable
internal fun rememberTextSelectionColors(colorScheme: Colors): TextSelectionColors {
    val primaryColor = colorScheme.primary
    return remember(primaryColor) {
        TextSelectionColors(
            handleColor = primaryColor,
            backgroundColor = primaryColor.copy(alpha = TextSelectionBackgroundOpacity),
        )
    }
}

internal const val TextSelectionBackgroundOpacity = 0.4f

private val LocalRevibesNavigator =
    compositionLocalOf<DestinationsNavigator> { error("No DestinationsNavigator provided") }
