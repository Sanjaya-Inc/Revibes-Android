/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.core.presentation.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.core.presentation.components.components.textfield.TextField

/**
 * Reusable validated text field for auth forms
 */
@Composable
fun AuthTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false,
    isEnabled: Boolean = true,
    errorText: String? = null,
    onImeAction: (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        enabled = isEnabled,
        isError = !errorText.isNullOrBlank(),
        supportingText = {
            if (!errorText.isNullOrBlank()) {
                Text(errorText)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction,
            showKeyboardOnFocus = true
        ),
        keyboardActions = if (onImeAction != null) {
            KeyboardActions(onDone = { onImeAction() })
        } else {
            KeyboardActions.Default
        },
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        modifier = modifier.padding(bottom = 16.dp)
    )
}

/**
 * Email input field with validation
 */
@Composable
fun EmailField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    errorText: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null
) {
    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        isEnabled = isEnabled,
        errorText = errorText,
        onImeAction = onImeAction,
        modifier = modifier
    )
}

/**
 * Password input field with validation
 */
@Composable
fun PasswordField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    errorText: String? = null,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null
) {
    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        keyboardType = KeyboardType.Password,
        imeAction = imeAction,
        isPassword = true,
        isEnabled = isEnabled,
        errorText = errorText,
        onImeAction = onImeAction,
        modifier = modifier
    )
}

/**
 * Phone input field with validation
 */
@Composable
fun PhoneField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    errorText: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null
) {
    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        keyboardType = KeyboardType.Phone,
        imeAction = imeAction,
        isEnabled = isEnabled,
        errorText = errorText,
        onImeAction = onImeAction,
        modifier = modifier
    )
}

/**
 * Primary action button for auth screens
 */
@Composable
fun AuthActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        text = text,
        onClick = onClick,
        enabled = isEnabled,
        loading = isLoading,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Navigation link row for switching between auth screens
 */
@Composable
fun AuthNavigationRow(
    text: String,
    actionText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text)
        TextButton(onClick = onActionClick) {
            Text(
                actionText,
                color = RevibesTheme.colors.secondary
            )
        }
    }
}
