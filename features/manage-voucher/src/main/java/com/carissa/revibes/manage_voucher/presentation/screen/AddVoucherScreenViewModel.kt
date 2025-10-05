package com.carissa.revibes.manage_voucher.presentation.screen

import android.util.Log
import android.content.Context
import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.manage_voucher.data.ManageVoucherRepository
import com.carissa.revibes.manage_voucher.domain.model.VoucherConditions
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import com.carissa.revibes.manage_voucher.presentation.handler.ManageVoucherExceptionHandler
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.android.annotation.KoinViewModel

data class AddVoucherScreenUiState(
    val isLoading: Boolean = false,
    val code: TextFieldValue = TextFieldValue(),
    val name: TextFieldValue = TextFieldValue(),
    val description: TextFieldValue = TextFieldValue(),
    val type: VoucherDomain.VoucherType = VoucherDomain.VoucherType.PERCENT_OFF,
    val amount: TextFieldValue = TextFieldValue(),
//    val currency: VoucherDomain.Currency = VoucherDomain.Currency.IDR,
    val maxClaim: TextFieldValue = TextFieldValue(),
    val maxUsage: TextFieldValue = TextFieldValue(),
    val minOrderItem: TextFieldValue = TextFieldValue(),
    val minOrderAmount: TextFieldValue = TextFieldValue(),
    val maxDiscountAmount: TextFieldValue = TextFieldValue(),
    val claimPeriodStart: String = "",
    val claimPeriodEnd: String = "",
    val imageUrl: String? = null,
    val selectedImageUri: Uri? = null,
    val isUploadingImage: Boolean = false,
    val showConditionsSection: Boolean = false,
    val termConditions: ImmutableList<String> = persistentListOf(),
    val guides: ImmutableList<String> = persistentListOf(),
    val showTermConditionsSection: Boolean = false,
    val showGuidesSection: Boolean = false,
    val codeError: String? = null,
    val nameError: String? = null,
    val descriptionError: String? = null,
    val amountError: String? = null,
    val maxClaimError: String? = null,
    val maxUsageError: String? = null,
    val minOrderItemError: String? = null,
    val minOrderAmountError: String? = null,
    val maxDiscountAmountError: String? = null,
    val claimPeriodStartError: String? = null,
    val claimPeriodEndError: String? = null,
    val imageError: String? = null,
    val termConditionsError: String? = null,
    val guidesError: String? = null
)

sealed interface AddVoucherScreenUiEvent {
    data object OnVoucherAddedSuccessfully : AddVoucherScreenUiEvent
    data object SaveVoucher : AddVoucherScreenUiEvent
    data class SaveVoucherWithContext(val context: Context) : AddVoucherScreenUiEvent
    data class CodeChanged(val code: TextFieldValue) : AddVoucherScreenUiEvent
    data class NameChanged(val name: TextFieldValue) : AddVoucherScreenUiEvent
    data class DescriptionChanged(val description: TextFieldValue) : AddVoucherScreenUiEvent
    data class TypeChanged(val type: VoucherDomain.VoucherType) : AddVoucherScreenUiEvent
    data class AmountChanged(val amount: TextFieldValue) : AddVoucherScreenUiEvent

    //    data class CurrencyChanged(val currency: VoucherDomain.Currency) : AddVoucherScreenUiEvent
    data class MaxClaimChanged(val maxClaim: TextFieldValue) : AddVoucherScreenUiEvent
    data class MaxUsageChanged(val maxUsage: TextFieldValue) : AddVoucherScreenUiEvent
    data class MinOrderItemChanged(val minOrderItem: TextFieldValue) : AddVoucherScreenUiEvent
    data class MinOrderAmountChanged(val minOrderAmount: TextFieldValue) : AddVoucherScreenUiEvent
    data class MaxDiscountAmountChanged(val maxDiscountAmount: TextFieldValue) :
        AddVoucherScreenUiEvent

    data class ClaimPeriodStartChanged(val date: String) : AddVoucherScreenUiEvent
    data class ClaimPeriodEndChanged(val date: String) : AddVoucherScreenUiEvent
    data class ImageUrlChanged(val imageUrl: String?) : AddVoucherScreenUiEvent
    data class ImageSelected(val uri: Uri) : AddVoucherScreenUiEvent
    data class UploadImage(val context: Context, val uri: Uri) : AddVoucherScreenUiEvent
    data class OnImageUploadSuccess(val imageUrl: String) : AddVoucherScreenUiEvent
    data class OnImageUploadFailed(val message: String) : AddVoucherScreenUiEvent
    data object RemoveImage : AddVoucherScreenUiEvent
    data object ToggleConditionsSection : AddVoucherScreenUiEvent
    data object ToggleTermConditionsSection : AddVoucherScreenUiEvent
    data object ToggleGuidesSection : AddVoucherScreenUiEvent
    data class AddTermCondition(val condition: String) : AddVoucherScreenUiEvent
    data class RemoveTermCondition(val index: Int) : AddVoucherScreenUiEvent
    data class UpdateTermCondition(val index: Int, val condition: String) : AddVoucherScreenUiEvent
    data class AddGuide(val guide: String) : AddVoucherScreenUiEvent
    data class RemoveGuide(val index: Int) : AddVoucherScreenUiEvent
    data class UpdateGuide(val index: Int, val guide: String) : AddVoucherScreenUiEvent
    data class OnCreateVoucherFailed(val message: String) : AddVoucherScreenUiEvent
}

@KoinViewModel
class AddVoucherScreenViewModel(
    private val repository: ManageVoucherRepository,
    private val exceptionHandler: ManageVoucherExceptionHandler
) : BaseViewModel<AddVoucherScreenUiState, AddVoucherScreenUiEvent>(
    initialState = AddVoucherScreenUiState(),
    exceptionHandler = { syntax, exception ->
        exceptionHandler.onAddVoucherError(syntax, exception)
    }
) {

    override fun onEvent(event: AddVoucherScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            AddVoucherScreenUiEvent.SaveVoucher -> saveVoucher()
            is AddVoucherScreenUiEvent.SaveVoucherWithContext -> saveVoucherWithContext(event.context)
            is AddVoucherScreenUiEvent.CodeChanged -> updateCode(event.code)
            is AddVoucherScreenUiEvent.NameChanged -> updateName(event.name)
            is AddVoucherScreenUiEvent.DescriptionChanged -> updateDescription(event.description)
            is AddVoucherScreenUiEvent.TypeChanged -> updateType(event.type)
            is AddVoucherScreenUiEvent.AmountChanged -> updateAmount(event.amount)
//            is AddVoucherScreenUiEvent.CurrencyChanged -> updateCurrency(event.currency)
            is AddVoucherScreenUiEvent.MaxClaimChanged -> updateMaxClaim(event.maxClaim)
            is AddVoucherScreenUiEvent.MaxUsageChanged -> updateMaxUsage(event.maxUsage)
            is AddVoucherScreenUiEvent.MinOrderItemChanged -> updateMinOrderItem(event.minOrderItem)
            is AddVoucherScreenUiEvent.MinOrderAmountChanged -> updateMinOrderAmount(event.minOrderAmount)
            is AddVoucherScreenUiEvent.MaxDiscountAmountChanged -> updateMaxDiscountAmount(event.maxDiscountAmount)
            is AddVoucherScreenUiEvent.ClaimPeriodStartChanged -> updateClaimPeriodStart(event.date)
            is AddVoucherScreenUiEvent.ClaimPeriodEndChanged -> updateClaimPeriodEnd(event.date)
            is AddVoucherScreenUiEvent.ImageUrlChanged -> updateImageUrl(event.imageUrl)
            is AddVoucherScreenUiEvent.ImageSelected -> updateSelectedImage(event.uri)
            is AddVoucherScreenUiEvent.UploadImage -> uploadImage(event.context, event.uri)
            is AddVoucherScreenUiEvent.OnImageUploadSuccess -> onImageUploadSuccess(event.imageUrl)
            is AddVoucherScreenUiEvent.OnImageUploadFailed -> onImageUploadFailed(event.message)
            AddVoucherScreenUiEvent.RemoveImage -> removeImage()
            AddVoucherScreenUiEvent.ToggleConditionsSection -> toggleConditionsSection()
            AddVoucherScreenUiEvent.ToggleTermConditionsSection -> toggleTermConditionsSection()
            AddVoucherScreenUiEvent.ToggleGuidesSection -> toggleGuidesSection()
            is AddVoucherScreenUiEvent.AddTermCondition -> addTermCondition(event.condition)
            is AddVoucherScreenUiEvent.RemoveTermCondition -> removeTermCondition(event.index)
            is AddVoucherScreenUiEvent.UpdateTermCondition -> updateTermCondition(event.index, event.condition)
            is AddVoucherScreenUiEvent.AddGuide -> addGuide(event.guide)
            is AddVoucherScreenUiEvent.RemoveGuide -> removeGuide(event.index)
            is AddVoucherScreenUiEvent.UpdateGuide -> updateGuide(event.index, event.guide)
            else -> Unit
        }
    }

    private fun updateCode(code: TextFieldValue) = intent {
        val formattedCode = code.text.uppercase().replace(" ", "-")
        reduce {
            state.copy(
                code = code.copy(text = formattedCode),
                codeError = null
            )
        }
    }

    private fun updateName(name: TextFieldValue) = intent {
        val formattedName = name.text.split(" ").joinToString(" ") {
            it.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase() else char.toString()
            }
        }
        reduce {
            state.copy(
                name = name.copy(text = formattedName),
                nameError = null
            )
        }
    }

    private fun updateDescription(description: TextFieldValue) = intent {
        val formattedDescription = description.text.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
        reduce {
            state.copy(
                description = description.copy(text = formattedDescription),
                descriptionError = null
            )
        }
    }

    private fun updateType(type: VoucherDomain.VoucherType) = intent {
        reduce { state.copy(type = type) }
    }

    private fun updateAmount(amount: TextFieldValue) = intent {
        reduce {
            state.copy(
                amount = amount,
                amountError = null
            )
        }
    }

//    private fun updateCurrency(currency: VoucherDomain.Currency) = intent {
//        reduce { state.copy(currency = currency) }
//    }

    private fun updateMaxClaim(maxClaim: TextFieldValue) = intent {
        reduce {
            state.copy(
                maxClaim = maxClaim,
                maxClaimError = null
            )
        }
    }

    private fun updateMaxDiscountAmount(maxDiscountAmount: TextFieldValue) = intent {
        reduce {
            state.copy(
                maxDiscountAmount = maxDiscountAmount,
                maxDiscountAmountError = null
            )
        }
    }

    private fun updateMaxUsage(maxUsage: TextFieldValue) = intent {
        reduce {
            state.copy(
                maxUsage = maxUsage,
                maxUsageError = null
            )
        }
    }

    private fun updateMinOrderItem(minOrderItem: TextFieldValue) = intent {
        reduce {
            state.copy(
                minOrderItem = minOrderItem,
                minOrderItemError = null
            )
        }
    }

    private fun updateMinOrderAmount(minOrderAmount: TextFieldValue) = intent {
        reduce {
            state.copy(
                minOrderAmount = minOrderAmount,
                minOrderAmountError = null
            )
        }
    }

    private fun updateClaimPeriodStart(date: String) = intent {
        reduce {
            state.copy(
                claimPeriodStart = date,
                claimPeriodStartError = null
            )
        }
    }

    private fun updateClaimPeriodEnd(date: String) = intent {
        reduce {
            state.copy(
                claimPeriodEnd = date,
                claimPeriodEndError = null
            )
        }
    }

    private fun updateImageUrl(imageUrl: String?) = intent {
        reduce { state.copy(imageUrl = imageUrl) }
    }

    private fun updateSelectedImage(uri: Uri) = intent {
        reduce {
            state.copy(
                selectedImageUri = uri,
                imageError = null
            )
        }
    }

    private fun uploadImage(context: Context, uri: Uri) = intent {
        reduce { state.copy(isUploadingImage = true) }

        try {
            // For now, we'll simulate the upload and use a placeholder URL
            // In a real implementation, you would upload to your server/cloud storage
            val imageUrl = "https://example.com/voucher-images/${System.currentTimeMillis()}.jpg"

            reduce {
                state.copy(
                    isUploadingImage = false,
                    imageUrl = imageUrl
                )
            }
            postSideEffect(AddVoucherScreenUiEvent.OnImageUploadSuccess(imageUrl))
        } catch (e: Exception) {
            reduce { state.copy(isUploadingImage = false) }
            postSideEffect(AddVoucherScreenUiEvent.OnImageUploadFailed(e.message ?: "Upload failed"))
        }
    }

    private fun onImageUploadSuccess(imageUrl: String) = intent {
        reduce {
            state.copy(
                imageUrl = imageUrl,
                isUploadingImage = false,
                imageError = null
            )
        }
    }

    private fun onImageUploadFailed(message: String) = intent {
        reduce {
            state.copy(
                isUploadingImage = false,
                imageError = message
            )
        }
    }

    private fun removeImage() = intent {
        reduce {
            state.copy(
                selectedImageUri = null,
                imageUrl = null,
                imageError = null
            )
        }
    }

    private fun toggleConditionsSection() = intent {
        reduce { state.copy(showConditionsSection = !state.showConditionsSection) }
    }

    private fun toggleTermConditionsSection() = intent {
        reduce { state.copy(showTermConditionsSection = !state.showTermConditionsSection) }
    }

    private fun toggleGuidesSection() = intent {
        reduce { state.copy(showGuidesSection = !state.showGuidesSection) }
    }

    private fun addTermCondition(condition: String) = intent {
        if (condition.isNotBlank()) {
            reduce {
                state.copy(
                    termConditions = (state.termConditions + condition).toImmutableList(),
                    termConditionsError = null
                )
            }
        }
    }

    private fun removeTermCondition(index: Int) = intent {
        if (index in state.termConditions.indices) {
            reduce {
                state.copy(
                    termConditions = state.termConditions.toMutableList().apply { removeAt(index) }.toImmutableList()
                )
            }
        }
    }

    private fun updateTermCondition(index: Int, condition: String) = intent {
        if (index in state.termConditions.indices) {
            reduce {
                state.copy(
                    termConditions = state.termConditions.toMutableList().apply {
                        set(
                            index,
                            condition
                        )
                    }.toImmutableList(),
                    termConditionsError = null
                )
            }
        }
    }

    private fun addGuide(guide: String) = intent {
        if (guide.isNotBlank()) {
            reduce {
                state.copy(
                    guides = (state.guides + guide).toImmutableList(),
                    guidesError = null
                )
            }
        }
    }

    private fun removeGuide(index: Int) = intent {
        if (index in state.guides.indices) {
            reduce {
                state.copy(guides = state.guides.toMutableList().apply { removeAt(index) }.toImmutableList())
            }
        }
    }

    private fun updateGuide(index: Int, guide: String) = intent {
        if (index in state.guides.indices) {
            reduce {
                state.copy(
                    guides = state.guides.toMutableList().apply { set(index, guide) }.toImmutableList(),
                    guidesError = null
                )
            }
        }
    }

    private fun saveVoucher() {
        intent {
            val isFormValid = validateForm(state)
            if (isFormValid) {
                reduce { state.copy(isLoading = true) }

                val conditions = if (state.showConditionsSection) {
                    VoucherConditions(
                        maxClaim = state.maxClaim.text.toIntOrNull() ?: 0,
                        maxUsage = state.maxUsage.text.toIntOrNull() ?: 0,
                        minOrderItem = state.minOrderItem.text.toIntOrNull() ?: 0,
                        minOrderAmount = state.minOrderAmount.text.toLongOrNull() ?: 0L,
                        maxDiscountAmount = state.maxDiscountAmount.text.toLongOrNull() ?: 0L
                    )
                } else {
                    VoucherConditions(
                        maxClaim = 0,
                        maxUsage = 0,
                        minOrderItem = 0,
                        minOrderAmount = 0L,
                        maxDiscountAmount = 0L
                    )
                }

                reduce { state.copy(isLoading = false) }
                postSideEffect(AddVoucherScreenUiEvent.OnVoucherAddedSuccessfully)
            } else {
                Log.e(TAG, "Form validation failed")
            }
        }
    }

    private fun saveVoucherWithContext(context: Context) {
        intent {
            val isFormValid = validateForm(state)
            val selectedImageUri = state.selectedImageUri
            if (isFormValid && selectedImageUri != null) {
                reduce { state.copy(isLoading = true) }

                val conditions = if (state.showConditionsSection) {
                    VoucherConditions(
                        maxClaim = state.maxClaim.text.toIntOrNull() ?: 0,
                        maxUsage = state.maxUsage.text.toIntOrNull() ?: 0,
                        minOrderItem = state.minOrderItem.text.toIntOrNull() ?: 0,
                        minOrderAmount = state.minOrderAmount.text.toLongOrNull() ?: 0L,
                        maxDiscountAmount = state.maxDiscountAmount.text.toLongOrNull() ?: 0L
                    )
                } else {
                    VoucherConditions(
                        maxClaim = 0,
                        maxUsage = 0,
                        minOrderItem = 0,
                        minOrderAmount = 0L,
                        maxDiscountAmount = 0L
                    )
                }

                repository.createVoucher(
                    context = context,
                    code = state.code.text,
                    name = state.name.text,
                    description = state.description.text,
                    type = state.type,
                    amount = state.amount.text.toDoubleOrNull() ?: 0.0,
                    conditions = conditions,
                    claimPeriodStart = state.claimPeriodStart,
                    claimPeriodEnd = state.claimPeriodEnd,
                    imageUri = selectedImageUri,
                    termConditions = state.termConditions,
                    guides = state.guides
                )

                reduce { state.copy(isLoading = false) }
                postSideEffect(AddVoucherScreenUiEvent.OnVoucherAddedSuccessfully)
            } else {
                Log.e(TAG, "Form validation failed or no image selected")
            }
        }
    }

    private fun validateForm(state: AddVoucherScreenUiState): Boolean {
        var hasError = false
        var newState = state.copy(
            codeError = null,
            nameError = null,
            descriptionError = null,
            amountError = null,
            maxClaimError = null,
            maxUsageError = null,
            minOrderItemError = null,
            minOrderAmountError = null,
            maxDiscountAmountError = null,
            claimPeriodStartError = null,
            claimPeriodEndError = null,
            imageError = null,
            termConditionsError = null,
            guidesError = null
        )

        if (state.code.text.isBlank()) {
            hasError = true
            newState = newState.copy(codeError = "Code is required")
        }

        if (state.name.text.isBlank()) {
            hasError = true
            newState = newState.copy(nameError = "Name is required")
        }

        if (state.description.text.isBlank()) {
            hasError = true
            newState = newState.copy(descriptionError = "Description is required")
        }

        if (state.amount.text.isBlank() || state.amount.text.toDoubleOrNull() == null) {
            hasError = true
            newState = newState.copy(amountError = "Valid amount is required")
        }

        if (state.maxClaim.text.isBlank() || state.maxClaim.text.toIntOrNull() == null) {
            hasError = true
            newState = newState.copy(maxClaimError = "Valid max claim is required")
        }
        if (state.maxUsage.text.isBlank() || state.maxUsage.text.toIntOrNull() == null) {
            hasError = true
            newState = newState.copy(maxUsageError = "Valid max usage is required")
        }
        if (state.minOrderItem.text.isBlank() || state.minOrderItem.text.toIntOrNull() == null) {
            hasError = true
            newState = newState.copy(minOrderItemError = "Valid min order item is required")
        }
        if (state.minOrderAmount.text.isBlank() || state.minOrderAmount.text.toLongOrNull() == null) {
            hasError = true
            newState = newState.copy(minOrderAmountError = "Valid min order amount is required")
        }
        if (state.maxDiscountAmount.text.isBlank() || state.maxDiscountAmount.text.toLongOrNull() == null) {
            hasError = true
            newState =
                newState.copy(maxDiscountAmountError = "Valid max discount amount is required")
        }

        if (state.claimPeriodStart.isBlank()) {
            hasError = true
            newState = newState.copy(claimPeriodStartError = "Start date is required")
        }

        if (state.claimPeriodEnd.isBlank()) {
            hasError = true
            newState = newState.copy(claimPeriodEndError = "End date is required")
        }

        if (state.selectedImageUri == null) {
            hasError = true
            newState = newState.copy(imageError = "Voucher image is required")
        }

        if (state.termConditions.isEmpty()) {
            hasError = true
            newState = newState.copy(termConditionsError = "At least one term condition is required")
        }

        if (state.guides.isEmpty()) {
            hasError = true
            newState = newState.copy(guidesError = "At least one guide is required")
        }

        intent { reduce { newState } }

        return !hasError
    }

    companion object {
        private const val TAG = "AddVoucherScreenViewMod"
    }
}
