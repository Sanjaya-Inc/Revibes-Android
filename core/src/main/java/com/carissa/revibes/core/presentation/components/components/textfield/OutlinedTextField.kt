package com.carissa.revibes.core.presentation.components.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.textfield.base.CommonDecorationBox
import com.carissa.revibes.core.presentation.components.components.textfield.base.FocusedOutlineThickness
import com.carissa.revibes.core.presentation.components.components.textfield.base.HorizontalIconPadding
import com.carissa.revibes.core.presentation.components.components.textfield.base.LabelBottomPadding
import com.carissa.revibes.core.presentation.components.components.textfield.base.SupportingTopPadding
import com.carissa.revibes.core.presentation.components.components.textfield.base.TextFieldColors
import com.carissa.revibes.core.presentation.components.components.textfield.base.TextFieldHorizontalPadding
import com.carissa.revibes.core.presentation.components.components.textfield.base.TextFieldMinHeight
import com.carissa.revibes.core.presentation.components.components.textfield.base.TextFieldVerticalPadding
import com.carissa.revibes.core.presentation.components.components.textfield.base.UnfocusedOutlineThickness
import com.carissa.revibes.core.presentation.components.components.textfield.base.containerOutline

@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = RevibesTheme.typography.input,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    shape: Shape = OutlinedTextFieldDefaults.Shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    cursorBrush: Brush = SolidColor(colors.cursorColor(isError).value),
) {
    val textColor =
        textStyle.color.takeOrElse {
            colors.textColor(enabled, isError, interactionSource).value
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        BasicTextField(
            modifier =
            modifier.defaultMinSize(
                minHeight = OutlinedTextFieldDefaults.MinHeight,
            ).fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    shape = shape,
                )
            },
        )
    }
}

@Composable
fun OutlinedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = RevibesTheme.typography.input,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    shape: Shape = OutlinedTextFieldDefaults.Shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    cursorBrush: Brush = SolidColor(colors.cursorColor(isError).value),
) {
    val textColor =
        textStyle.color.takeOrElse {
            colors.textColor(enabled, isError, interactionSource).value
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        BasicTextField(
            modifier =
            modifier
                .defaultMinSize(
                    minHeight = OutlinedTextFieldDefaults.MinHeight,
                )
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value.text,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    shape = shape,
                )
            },
        )
    }
}

@Immutable
object OutlinedTextFieldDefaults {
    val MinHeight = TextFieldMinHeight
    val Shape: Shape = CircleShape

    private fun contentPadding(
        start: Dp = TextFieldHorizontalPadding,
        end: Dp = TextFieldHorizontalPadding,
        top: Dp = TextFieldVerticalPadding,
        bottom: Dp = TextFieldVerticalPadding,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    private fun labelPadding(
        start: Dp = 0.dp,
        top: Dp = 0.dp,
        end: Dp = 0.dp,
        bottom: Dp = LabelBottomPadding,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    private fun supportingTextPadding(
        start: Dp = 0.dp,
        top: Dp = SupportingTopPadding,
        end: Dp = TextFieldHorizontalPadding,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    private fun leadingIconPadding(
        start: Dp = HorizontalIconPadding,
        top: Dp = 0.dp,
        end: Dp = 0.dp,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    private fun trailingIconPadding(
        start: Dp = 0.dp,
        top: Dp = 0.dp,
        end: Dp = HorizontalIconPadding,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    fun containerBorderThickness(
        interactionSource: InteractionSource,
    ): Dp {
        val focused by interactionSource.collectIsFocusedAsState()

        return if (focused) FocusedOutlineThickness else UnfocusedOutlineThickness
    }

    @Composable
    fun DecorationBox(
        value: String,
        innerTextField: @Composable () -> Unit,
        enabled: Boolean,
        visualTransformation: VisualTransformation,
        interactionSource: InteractionSource,
        modifier: Modifier = Modifier,
        isError: Boolean = false,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        prefix: @Composable (() -> Unit)? = null,
        suffix: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        shape: Shape = Shape,
        colors: TextFieldColors = colors(),
        container: @Composable () -> Unit = {
            ContainerBox(enabled, isError, interactionSource, colors, shape = shape)
        },
    ) {
        CommonDecorationBox(
            value = value,
            innerTextField = innerTextField,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            enabled = enabled,
            isError = isError,
            interactionSource = interactionSource,
            colors = colors,
            contentPadding = contentPadding(),
            labelPadding = labelPadding(),
            supportingTextPadding = supportingTextPadding(),
            leadingIconPadding = leadingIconPadding(),
            trailingIconPadding = trailingIconPadding(),
            container = container,
            modifier = modifier
        )
    }

    @Composable
    fun ContainerBox(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
        colors: TextFieldColors,
        modifier: Modifier = Modifier,
        shape: Shape = Shape,
        borderThickness: Dp = containerBorderThickness(interactionSource),
    ) {
        Box(
            modifier
                .background(colors.containerColor(enabled, isError, interactionSource).value, shape)
                .containerOutline(enabled, isError, interactionSource, colors, borderThickness, shape),
        )
    }

    @Composable
    fun colors(): TextFieldColors {
        return TextFieldColors(
            focusedTextColor = RevibesTheme.colors.text,
            unfocusedTextColor = RevibesTheme.colors.text,
            disabledTextColor = RevibesTheme.colors.onDisabled,
            errorTextColor = RevibesTheme.colors.text,
            focusedContainerColor = RevibesTheme.colors.primary,
            unfocusedContainerColor = RevibesTheme.colors.primary,
            disabledContainerColor = RevibesTheme.colors.primary,
            errorContainerColor = RevibesTheme.colors.primary,
            cursorColor = RevibesTheme.colors.primary,
            errorCursorColor = RevibesTheme.colors.error,
            textSelectionColors = LocalTextSelectionColors.current,
            focusedOutlineColor = RevibesTheme.colors.primary,
            unfocusedOutlineColor = RevibesTheme.colors.secondary,
            disabledOutlineColor = RevibesTheme.colors.disabled,
            errorOutlineColor = RevibesTheme.colors.error,
            focusedLeadingIconColor = RevibesTheme.colors.primary,
            unfocusedLeadingIconColor = RevibesTheme.colors.primary,
            disabledLeadingIconColor = RevibesTheme.colors.onDisabled,
            errorLeadingIconColor = RevibesTheme.colors.primary,
            focusedTrailingIconColor = RevibesTheme.colors.primary,
            unfocusedTrailingIconColor = RevibesTheme.colors.primary,
            disabledTrailingIconColor = RevibesTheme.colors.onDisabled,
            errorTrailingIconColor = RevibesTheme.colors.primary,
            focusedLabelColor = RevibesTheme.colors.primary,
            unfocusedLabelColor = RevibesTheme.colors.primary,
            disabledLabelColor = RevibesTheme.colors.textDisabled,
            errorLabelColor = RevibesTheme.colors.error,
            focusedPlaceholderColor = RevibesTheme.colors.textSecondary,
            unfocusedPlaceholderColor = RevibesTheme.colors.textSecondary,
            disabledPlaceholderColor = RevibesTheme.colors.textDisabled,
            errorPlaceholderColor = RevibesTheme.colors.textSecondary,
            focusedSupportingTextColor = RevibesTheme.colors.primary,
            unfocusedSupportingTextColor = RevibesTheme.colors.primary,
            disabledSupportingTextColor = RevibesTheme.colors.textDisabled,
            errorSupportingTextColor = RevibesTheme.colors.error,
            focusedPrefixColor = RevibesTheme.colors.primary,
            unfocusedPrefixColor = RevibesTheme.colors.primary,
            disabledPrefixColor = RevibesTheme.colors.onDisabled,
            errorPrefixColor = RevibesTheme.colors.primary,
            focusedSuffixColor = RevibesTheme.colors.primary,
            unfocusedSuffixColor = RevibesTheme.colors.primary,
            disabledSuffixColor = RevibesTheme.colors.onDisabled,
            errorSuffixColor = RevibesTheme.colors.primary,
        )
    }
}
