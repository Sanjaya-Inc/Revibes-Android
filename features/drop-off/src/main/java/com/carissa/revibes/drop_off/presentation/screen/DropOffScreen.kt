/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.drop_off.presentation.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.DropOffDialogBg
import com.carissa.revibes.core.presentation.components.DropOffDialogItemBg
import com.carissa.revibes.core.presentation.components.DropOffLabelColor
import com.carissa.revibes.core.presentation.components.DropOffPlaceholderColor
import com.carissa.revibes.core.presentation.components.DropOffTextFieldBg
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.core.presentation.components.components.RevibesLoading
import com.carissa.revibes.core.presentation.components.components.textfield.OutlinedTextField
import com.carissa.revibes.core.presentation.components.components.textfield.OutlinedTextFieldDefaults
import com.carissa.revibes.drop_off.R
import com.carissa.revibes.drop_off.domain.model.StoreData
import com.carissa.revibes.drop_off.presentation.navigation.DropOffGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<DropOffGraph>(start = true)
@Composable
fun DropOffScreen(
    modifier: Modifier = Modifier,
    viewModel: DropOffScreenViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    viewModel.collectSideEffect { event ->
        when (event) {
            is DropOffScreenUiEvent.OnLoadDropOffDataFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is DropOffScreenUiEvent.OnImageUploadFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            CommonHeader(
                title = context.getString(R.string.drop_off_title),
                backgroundDrawRes = R.drawable.bg_drop_off,
                searchTextFieldValue = searchText,
                onTextChange = { searchText = it },
                onProfileClicked = {},
            )
        }
    ) { contentPadding ->
        if (state.isLoading || state.currentOrderId.isNullOrBlank()) {
            RevibesLoading(modifier = Modifier.padding(contentPadding))
        } else {
            DropOffScreenContent(
                modifier = Modifier
                    .padding(contentPadding)
                    .background(RevibesTheme.colors.background),
                orderId = state.currentOrderId.orEmpty(),
                items = state.items,
                name = state.name,
                eventReceiver = viewModel,
                nearestStores = state.stores,
                selectedStore = state.selectedStore,
                isFormValid = state.isFormValid,
                validationErrors = state.validationErrors
            )
        }
    }
}

@Composable
fun DropOffScreenContent(
    modifier: Modifier = Modifier,
    orderId: String = "",
    items: ImmutableList<DropOffItem> = persistentListOf(),
    name: TextFieldValue = TextFieldValue(""),
    eventReceiver: EventReceiver<DropOffScreenUiEvent> = EventReceiver { },
    nearestStores: ImmutableList<StoreData> = persistentListOf(),
    selectedStore: StoreData? = null,
    isFormValid: Boolean = false,
    validationErrors: ValidationErrors = ValidationErrors(),
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showStoreDropdown by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            context.getString(R.string.name_label),
            color = DropOffLabelColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        OutlinedTextField(
            value = name,
            singleLine = true,
            onValueChange = { eventReceiver.onEvent(DropOffScreenUiEvent.OnNameChange(it)) },
            placeholder = {
                Text(
                    context.getString(R.string.name_placeholder),
                    color = DropOffPlaceholderColor
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words
            ),
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedContainerColor = DropOffTextFieldBg,
                unfocusedContainerColor = DropOffTextFieldBg,
                disabledContainerColor = DropOffTextFieldBg,
                errorContainerColor = DropOffTextFieldBg,
                unfocusedOutlineColor = if (validationErrors.nameError != null) Color.Red else DropOffTextFieldBg,
                focusedOutlineColor = if (validationErrors.nameError != null) Color.Red else RevibesTheme.colors.primary
            ),
            shape = RoundedCornerShape(16.dp),
            isError = validationErrors.nameError != null
        )
        validationErrors.nameError?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                style = RevibesTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        Text(
            context.getString(R.string.location_drop_off_label),
            color = DropOffLabelColor,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        )

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            OutlinedTextField(
                value = selectedStore?.name.orEmpty(),
                onValueChange = { },
                placeholder = {
                    Text(
                        context.getString(R.string.select_location_placeholder),
                        color = DropOffPlaceholderColor
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedContainerColor = DropOffTextFieldBg,
                    unfocusedContainerColor = DropOffTextFieldBg,
                    disabledContainerColor = DropOffTextFieldBg,
                    errorContainerColor = DropOffTextFieldBg,
                    unfocusedOutlineColor = if (validationErrors.storeError != null) {
                        Color.Red
                    } else {
                        DropOffTextFieldBg
                    },
                    focusedOutlineColor = if (validationErrors.storeError != null) {
                        Color.Red
                    } else {
                        RevibesTheme.colors.primary
                    }
                ),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = context.getString(R.string.dropdown_icon_desc),
                        tint = RevibesTheme.colors.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                readOnly = true,
                isError = validationErrors.storeError != null
            )
            Box(
                Modifier
                    .matchParentSize()
                    .clickable {
                        showStoreDropdown = true
                        focusManager.clearFocus()
                    }
                    .background(Color.Transparent)
            )
        }
        validationErrors.storeError?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                style = RevibesTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        if (showStoreDropdown) {
            AlertDialog(
                onDismissRequest = { showStoreDropdown = false },
                confirmButton = {},
                containerColor = DropOffDialogBg,
                title = {
                    Text(
                        text = context.getString(R.string.nearest_delivery_location_title),
                        style = RevibesTheme.typography.h2,
                        color = RevibesTheme.colors.onPrimary
                    )
                },
                text = {
                    LazyColumn {
                        items(nearestStores) { loc ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        eventReceiver.onEvent(
                                            DropOffScreenUiEvent.OnStoreSelected(loc)
                                        )
                                        showStoreDropdown = false
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = DropOffDialogItemBg
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = context.getString(R.string.location_icon_desc),
                                            tint = RevibesTheme.colors.primary,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .padding(end = 4.dp)
                                        )
                                        Text(
                                            text = context.getString(
                                                R.string.distance_format,
                                                loc.distance
                                            ),
                                            style = RevibesTheme.typography.body2,
                                            color = RevibesTheme.colors.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
            )
        }
        OutlinedTextField(
            value = selectedStore?.address.orEmpty(),
            onValueChange = {},
            placeholder = {
                Text(
                    context.getString(R.string.address_placeholder),
                    color = DropOffLabelColor
                )
            },
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
                unfocusedOutlineColor = DropOffTextFieldBg
            ),
            shape = RoundedCornerShape(16.dp),
            readOnly = true
        )
        OutlinedTextField(
            value = selectedStore?.postalCode ?: "",
            onValueChange = {},
            singleLine = true,
            placeholder = {
                Text(
                    context.getString(R.string.postal_code_placeholder),
                    color = DropOffLabelColor
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedContainerColor = DropOffTextFieldBg,
                unfocusedContainerColor = DropOffTextFieldBg,
                disabledContainerColor = DropOffTextFieldBg,
                errorContainerColor = DropOffTextFieldBg,
                unfocusedOutlineColor = DropOffTextFieldBg
            ),
            shape = RoundedCornerShape(16.dp),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = context.getString(R.string.item_label),
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
                onItemChange = {
                    eventReceiver.onEvent(
                        DropOffScreenUiEvent.UpdateItem(
                            index,
                            it
                        )
                    )
                },
                onRemove = { eventReceiver.onEvent(DropOffScreenUiEvent.RemoveItem(index)) },
                onImageUpload = { imageUri, contentType ->
                    eventReceiver.onEvent(
                        DropOffScreenUiEvent.UploadImage(
                            context = context,
                            orderId = orderId,
                            itemId = item.id,
                            itemIndex = index,
                            imageUri = imageUri,
                            contentType = contentType
                        )
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        val primaryColor = RevibesTheme.colors.primary
        OutlinedButton(
            onClick = { eventReceiver.onEvent(DropOffScreenUiEvent.AddItemToOrder(orderId)) },
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
            Text(
                text = context.getString(R.string.add_item_button),
                color = RevibesTheme.colors.primary
            )
        }
        validationErrors.itemsError?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                style = RevibesTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                onClick = { eventReceiver.onEvent(DropOffScreenUiEvent.MakeOrder) },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (isFormValid) RevibesTheme.colors.primary else Color.Gray,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            ) {
                Text(context.getString(R.string.make_order_button))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemSection(
    item: DropOffItem,
    index: Int,
    onItemChange: (DropOffItem) -> Unit,
    onRemove: () -> Unit,
    onImageUpload: (Uri, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isItemNameValid = item.name.trim().isNotEmpty()
    val isItemTypeValid = item.type.isNotEmpty()
    val isItemWeightValid = item.weight != null
    val isItemPhotosValid = item.photos.isNotEmpty()
    val context = LocalContext.current
    val typeOptions = listOf(
        context.getString(R.string.organic_option) to context.getString(R.string.organic_type_value),
        context.getString(R.string.non_organic_option) to context.getString(R.string.non_organic_type_value),
        context.getString(R.string.b3_option) to context.getString(R.string.b3_type_value)
    )
    var expanded by remember { mutableStateOf(false) }
    val weightOptions = listOf(
        context.getString(R.string.weight_less_1kg) to 1,
        context.getString(R.string.weight_4_6kg) to 5,
        context.getString(R.string.weight_7_9kg) to 8,
        context.getString(R.string.weight_more_10kg) to 10
    )
    val selectedTypeLabel = typeOptions.find { it.second == item.type }?.first ?: context.getString(
        R.string.empty_string
    )
    var weightExpanded by remember { mutableStateOf(false) }
    val selectedWeightLabel =
        weightOptions.find { it.second == item.weight?.second }?.first ?: context.getString(
            R.string.empty_string
        )

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val contentType = context.contentResolver.getType(selectedUri) ?: context.getString(
                R.string.default_image_content_type
            )
            onImageUpload(selectedUri, contentType)
        }
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = modifier,
        color = RevibesTheme.colors.background
    ) {
        Column {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = context.getString(R.string.item_number, index + 1),
                    color = DropOffLabelColor,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp, bottom = 4.dp)
                )
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = context.getString(R.string.remove_item_desc)
                    )
                }
            }
            OutlinedTextField(
                value = item.name,
                onValueChange = { onItemChange(item.copy(name = it)) },
                placeholder = {
                    Text(
                        context.getString(R.string.item_name_placeholder),
                        color = DropOffPlaceholderColor
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words
                ),
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedContainerColor = DropOffTextFieldBg,
                    unfocusedContainerColor = DropOffTextFieldBg,
                    disabledContainerColor = DropOffTextFieldBg,
                    errorContainerColor = DropOffTextFieldBg,
                    unfocusedOutlineColor = if (!isItemNameValid) Color.Red else DropOffTextFieldBg,
                    focusedOutlineColor = if (!isItemNameValid) Color.Red else RevibesTheme.colors.primary
                ),
                shape = RoundedCornerShape(16.dp),
                isError = !isItemNameValid
            )
            if (!isItemNameValid) {
                Text(
                    text = context.getString(R.string.item_name_required_error),
                    color = Color.Red,
                    style = RevibesTheme.typography.body2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = context.getString(R.string.type_number, index + 1),
                        color = DropOffLabelColor,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedTypeLabel,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = {
                                Text(
                                    context.getString(R.string.organic_placeholder),
                                    color = DropOffPlaceholderColor
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors().copy(
                                focusedContainerColor = DropOffTextFieldBg,
                                unfocusedContainerColor = DropOffTextFieldBg,
                                disabledContainerColor = DropOffTextFieldBg,
                                errorContainerColor = DropOffTextFieldBg,
                                unfocusedOutlineColor = if (!isItemTypeValid) Color.Red else DropOffTextFieldBg,
                                focusedOutlineColor = if (!isItemTypeValid) Color.Red else RevibesTheme.colors.primary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            isError = !isItemTypeValid
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            typeOptions.forEach { (label, option) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        onItemChange(item.copy(type = option))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        text = context.getString(R.string.weight_number, index + 1),
                        color = DropOffLabelColor,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = weightExpanded,
                        onExpandedChange = { weightExpanded = !weightExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedWeightLabel,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = {
                                Text(
                                    context.getString(R.string.weight_placeholder),
                                    color = DropOffPlaceholderColor
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = weightExpanded) },
                            modifier = Modifier
                                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors().copy(
                                focusedContainerColor = DropOffTextFieldBg,
                                unfocusedContainerColor = DropOffTextFieldBg,
                                disabledContainerColor = DropOffTextFieldBg,
                                errorContainerColor = DropOffTextFieldBg,
                                unfocusedOutlineColor = if (!isItemWeightValid) Color.Red else DropOffTextFieldBg,
                                focusedOutlineColor = if (!isItemWeightValid) Color.Red else RevibesTheme.colors.primary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            isError = !isItemWeightValid
                        )
                        ExposedDropdownMenu(
                            expanded = weightExpanded,
                            onDismissRequest = { weightExpanded = false }
                        ) {
                            weightOptions.forEach { weightOption ->
                                val (label, _) = weightOption
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        onItemChange(item.copy(weight = weightOption))
                                        weightExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Text(
                text = context.getString(R.string.upload_photo_number, index + 1),
                color = DropOffLabelColor,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            if (item.photos.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(item.photos) { photoUrl ->
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = context.getString(R.string.uploaded_photo_desc),
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .background(
                        color = DropOffTextFieldBg,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        imagePickerLauncher.launch(context.getString(R.string.image_mime_type))
                    }
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = context.getString(R.string.upload_photo_desc),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                )
                Text(
                    text = if (item.photos.isEmpty()) {
                        context.getString(R.string.upload_photo_button)
                    } else {
                        context.getString(R.string.add_more_photos_button)
                    },
                    color = DropOffPlaceholderColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
            if (!isItemPhotosValid) {
                Text(
                    text = context.getString(R.string.photo_required_error),
                    color = Color.Red,
                    style = RevibesTheme.typography.body2,
                    modifier = Modifier.padding(top = 4.dp)
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
                id = "item_0",
                name = "",
                type = "",
                weight = "Label" to 1
            ),
            index = 0,
            onItemChange = {},
            onRemove = {},
            onImageUpload = { _, _ -> },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
