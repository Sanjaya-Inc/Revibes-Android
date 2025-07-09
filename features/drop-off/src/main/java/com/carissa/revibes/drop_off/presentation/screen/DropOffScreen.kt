/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.drop_off.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.components.DropOffLabelColor
import com.carissa.revibes.core.presentation.components.DropOffPlaceholderColor
import com.carissa.revibes.core.presentation.components.DropOffTextFieldBg
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.core.presentation.components.components.textfield.OutlinedTextField
import com.carissa.revibes.core.presentation.components.components.textfield.OutlinedTextFieldDefaults
import com.carissa.revibes.drop_off.presentation.navigation.DropOffGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@Destination<DropOffGraph>(start = true)
@Composable
fun DropOffScreen(
    modifier: Modifier = Modifier,
    viewModel: DropOffScreenViewModel = koinViewModel()
) {
    val items by viewModel.items.collectAsState()
    DropOffScreenContent(
        modifier = modifier.background(RevibesTheme.colors.background),
        items = items.toImmutableList(),
        name = viewModel.name,
        onNameChange = viewModel::onNameChange,
        addItem = viewModel::addItem,
        makeOrder = viewModel::makeOrder,
        updateItem = viewModel::updateItem,
        removeItem = viewModel::removeItem,
        nearestLocations = viewModel.nearestLocations,
        onLocationSelected = viewModel::onLocationSelected,
        selectedLocation = viewModel.selectedLocation
    )
}

@Composable
fun DropOffScreenContent(
    modifier: Modifier = Modifier,
    items: ImmutableList<DropOffItem> = persistentListOf(),
    name: TextFieldValue = TextFieldValue(""),
    onNameChange: (TextFieldValue) -> Unit = {},
    addItem: () -> Unit = {},
    makeOrder: () -> Unit = {},
    updateItem: (Int, DropOffItem) -> Unit = { _, _ -> },
    removeItem: (Int) -> Unit = {},
    nearestLocations: ImmutableList<LocationInfo> = persistentListOf(),
    onLocationSelected: (LocationInfo) -> Unit = {},
    selectedLocation: LocationInfo? = null,
) {
    val scrollState = rememberScrollState()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    // Dropdown state for location field
    var showLocationDropdown by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        CommonHeader(
            title = "Drop Off",
            backgroundDrawRes = R.drawable.bg_drop_off,
            searchTextFieldValue = searchText,
            onTextChange = { searchText = it },
            onBackClicked = {},
            onProfileClicked = {},
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Name",
            color = DropOffLabelColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        OutlinedTextField(
            value = name,
            singleLine = true,
            onValueChange = onNameChange,
            placeholder = { Text("Name", color = DropOffPlaceholderColor) },
            modifier = Modifier.padding(horizontal = 16.dp),
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedContainerColor = DropOffTextFieldBg,
                unfocusedContainerColor = DropOffTextFieldBg,
                disabledContainerColor = DropOffTextFieldBg,
                errorContainerColor = DropOffTextFieldBg,
                unfocusedOutlineColor = RevibesTheme.colors.primary,
            ),
            shape = RoundedCornerShape(16.dp)
        )
        Text(
            "Location Drop Off",
            color = DropOffLabelColor,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        )

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            OutlinedTextField(
                value = selectedLocation?.name.orEmpty(),
                onValueChange = { },
                placeholder = { Text("Select location", color = DropOffPlaceholderColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedContainerColor = DropOffTextFieldBg,
                    unfocusedContainerColor = DropOffTextFieldBg,
                    disabledContainerColor = DropOffTextFieldBg,
                    errorContainerColor = DropOffTextFieldBg,
                    unfocusedOutlineColor = RevibesTheme.colors.primary,
                ),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        tint = RevibesTheme.colors.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                readOnly = true
            )
            Box(
                Modifier
                    .matchParentSize()
                    .clickable {
                        showLocationDropdown = true
                        focusManager.clearFocus()
                    }
                    .background(Color.Transparent)
            )
        }
        if (showLocationDropdown) {
            AlertDialog(
                onDismissRequest = { showLocationDropdown = false },
                confirmButton = {},
                title = {
                    Text(
                        text = "Nearest Delivery Location",
                        style = RevibesTheme.typography.h2,
                        color = RevibesTheme.colors.text
                    )
                },
                text = {
                    LazyColumn {
                        items(nearestLocations) { loc ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        onLocationSelected(loc)
                                        showLocationDropdown = false
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = RevibesTheme.colors.surface
                                )
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        text = loc.name,
                                        style = RevibesTheme.typography.h3,
                                        color = RevibesTheme.colors.text
                                    )
                                    Text(
                                        text = loc.address,
                                        style = RevibesTheme.typography.body2,
                                        color = RevibesTheme.colors.text
                                    )
                                    Text(
                                        text = String.format("%.1f km", loc.distanceKm),
                                        style = RevibesTheme.typography.body2,
                                        color = RevibesTheme.colors.primary
                                    )
                                }
                            }
                        }
                    }
                },
            )
        }
        OutlinedTextField(
            value = selectedLocation?.address.orEmpty(),
            onValueChange = {},
            placeholder = { Text("Address", color = DropOffLabelColor) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            minLines = 3,
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedContainerColor = DropOffTextFieldBg,
                unfocusedContainerColor = DropOffTextFieldBg,
                disabledContainerColor = DropOffTextFieldBg,
                errorContainerColor = DropOffTextFieldBg,
                unfocusedOutlineColor = RevibesTheme.colors.primary,
            ),
            shape = RoundedCornerShape(16.dp),
            readOnly = true
        )
        OutlinedTextField(
            value = selectedLocation?.postalCode?.toString() ?: "",
            onValueChange = {},
            singleLine = true,
            placeholder = { Text("Postal Code", color = DropOffLabelColor) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedContainerColor = DropOffTextFieldBg,
                unfocusedContainerColor = DropOffTextFieldBg,
                disabledContainerColor = DropOffTextFieldBg,
                errorContainerColor = DropOffTextFieldBg,
                unfocusedOutlineColor = RevibesTheme.colors.primary,
            ),
            shape = RoundedCornerShape(16.dp),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ITEM",
            style = RevibesTheme.typography.h2,
            color = DropOffLabelColor,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        items.forEachIndexed { index, item ->
            ItemSection(
                item = item,
                index = index,
                onItemChange = { updateItem(index, it) },
                onRemove = { removeItem(index) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        val primaryColor = RevibesTheme.colors.primary
        OutlinedButton(
            onClick = addItem,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val dashWidth = 8.dp.toPx()
                    val dashGap = 4.dp.toPx()
                    val cornerRadius = 20.dp.toPx()

                    drawRoundRect(
                        color = primaryColor,
                        style = Stroke(
                            width = strokeWidth,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(dashWidth, dashGap),
                                0f
                            )
                        ),
                        cornerRadius = CornerRadius(cornerRadius)
                    )
                },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = RevibesTheme.colors.primary
            ),
            border = null
        ) {
            Text(text = "Add Item", color = RevibesTheme.colors.primary)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                onClick = makeOrder,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = RevibesTheme.colors.primary,
                    contentColor = Color.White
                )
            ) {
                Text("MAKE ORDER")
            }
        }
    }
}

@Composable
private fun ItemSection(
    item: DropOffItem,
    index: Int,
    onItemChange: (DropOffItem) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = modifier,
        color = RevibesTheme.colors.background
    ) {
        Column {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "Item ${index + 1}",
                    color = DropOffLabelColor,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp, bottom = 4.dp)
                )
                if (index != 0) {
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove Item")
                    }
                }
            }
            OutlinedTextField(
                value = item.name,
                onValueChange = { onItemChange(item.copy(name = it)) },
                placeholder = { Text("Daun Kering", color = DropOffPlaceholderColor) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedContainerColor = DropOffTextFieldBg,
                    unfocusedContainerColor = DropOffTextFieldBg,
                    disabledContainerColor = DropOffTextFieldBg,
                    errorContainerColor = DropOffTextFieldBg,
                    unfocusedOutlineColor = RevibesTheme.colors.primary,
                ),
                shape = RoundedCornerShape(16.dp)
            )
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Type ${index + 1}",
                        color = DropOffLabelColor,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = item.type,
                        onValueChange = { onItemChange(item.copy(type = it)) },
                        singleLine = true,
                        placeholder = { Text("Organic", color = DropOffPlaceholderColor) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors().copy(
                            focusedContainerColor = DropOffTextFieldBg,
                            unfocusedContainerColor = DropOffTextFieldBg,
                            disabledContainerColor = DropOffTextFieldBg,
                            errorContainerColor = DropOffTextFieldBg,
                            unfocusedOutlineColor = RevibesTheme.colors.primary,
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Weight ${index + 1} (Estimation)",
                        color = DropOffLabelColor,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = item.weight,
                        onValueChange = { onItemChange(item.copy(weight = it)) },
                        singleLine = true,
                        placeholder = { Text("> 1kg", color = DropOffPlaceholderColor) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = OutlinedTextFieldDefaults.colors().copy(
                            focusedContainerColor = DropOffTextFieldBg,
                            unfocusedContainerColor = DropOffTextFieldBg,
                            disabledContainerColor = DropOffTextFieldBg,
                            errorContainerColor = DropOffTextFieldBg,
                            unfocusedOutlineColor = RevibesTheme.colors.primary,
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
            Text(
                text = "Upload Photo or Video ${index + 1}",
                color = DropOffLabelColor,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Column(
                modifier = Modifier
                    .background(
                        color = DropOffTextFieldBg,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = "Upload Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                )
                Text(
                    text = "Upload Photo",
                    color = DropOffPlaceholderColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
private fun DropOffScreenPreview() {
    RevibesTheme {
        DropOffScreenContent(
            modifier = Modifier.background(Color.White),
        )
    }
}

@Preview
@Composable
private fun ItemSectionPreview() {
    RevibesTheme {
        ItemSection(
            item = DropOffItem(
                name = "",
                type = "",
                weight = ""
            ),
            index = 0,
            onItemChange = {},
            onRemove = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
