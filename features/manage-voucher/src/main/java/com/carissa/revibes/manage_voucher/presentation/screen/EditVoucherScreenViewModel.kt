package com.carissa.revibes.manage_voucher.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.manage_voucher.data.ManageVoucherRepository
import com.carissa.revibes.manage_voucher.domain.model.VoucherConditions
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import com.carissa.revibes.manage_voucher.presentation.handler.ManageVoucherExceptionHandler
import org.koin.android.annotation.KoinViewModel

data class EditVoucherScreenUiState(
    val isLoading: Boolean = false,
    val isLoadingVoucher: Boolean = true,
    val voucher: VoucherDomain? = null,
    val code: TextFieldValue = TextFieldValue(),
    val name: TextFieldValue = TextFieldValue(),
    val description: TextFieldValue = TextFieldValue(),
    val type: VoucherDomain.VoucherType = VoucherDomain.VoucherType.PERCENT_OFF,
    val amount: TextFieldValue = TextFieldValue(),
    val currency: VoucherDomain.Currency = VoucherDomain.Currency.IDR,
    val maxClaim: TextFieldValue = TextFieldValue(),
    val maxUsage: TextFieldValue = TextFieldValue(),
    val minOrderItem: TextFieldValue = TextFieldValue(),
    val minOrderAmount: TextFieldValue = TextFieldValue(),
    val maxDiscountAmount: TextFieldValue = TextFieldValue(),
    val claimPeriodStart: String = "",
    val claimPeriodEnd: String = "",
    val imageUrl: String? = null,
    val isActive: Boolean = true,
    val showConditionsSection: Boolean = false,
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
    val claimPeriodEndError: String? = null
)

sealed interface EditVoucherScreenUiEvent : NavigationEvent {
    data class Initialize(val voucherId: String) : EditVoucherScreenUiEvent
    data object NavigateBack : EditVoucherScreenUiEvent
    data object SaveVoucher : EditVoucherScreenUiEvent
    data class CodeChanged(val code: TextFieldValue) : EditVoucherScreenUiEvent
    data class NameChanged(val name: TextFieldValue) : EditVoucherScreenUiEvent
    data class DescriptionChanged(val description: TextFieldValue) : EditVoucherScreenUiEvent
    data class TypeChanged(val type: VoucherDomain.VoucherType) : EditVoucherScreenUiEvent
    data class AmountChanged(val amount: TextFieldValue) : EditVoucherScreenUiEvent
    data class CurrencyChanged(val currency: VoucherDomain.Currency) : EditVoucherScreenUiEvent
    data class MaxClaimChanged(val maxClaim: TextFieldValue) : EditVoucherScreenUiEvent
    data class MaxUsageChanged(val maxUsage: TextFieldValue) : EditVoucherScreenUiEvent
    data class MinOrderItemChanged(val minOrderItem: TextFieldValue) : EditVoucherScreenUiEvent
    data class MinOrderAmountChanged(val minOrderAmount: TextFieldValue) : EditVoucherScreenUiEvent
    data class MaxDiscountAmountChanged(val maxDiscountAmount: TextFieldValue) :
        EditVoucherScreenUiEvent

    data class ClaimPeriodStartChanged(val date: String) : EditVoucherScreenUiEvent
    data class ClaimPeriodEndChanged(val date: String) : EditVoucherScreenUiEvent
    data class ImageUrlChanged(val imageUrl: String?) : EditVoucherScreenUiEvent
    data class IsActiveChanged(val isActive: Boolean) : EditVoucherScreenUiEvent
    data object ToggleConditionsSection : EditVoucherScreenUiEvent
    data class OnUpdateVoucherFailed(val message: String) : EditVoucherScreenUiEvent
}

@KoinViewModel
class EditVoucherScreenViewModel(
    private val repository: ManageVoucherRepository,
    private val exceptionHandler: ManageVoucherExceptionHandler
) : BaseViewModel<EditVoucherScreenUiState, EditVoucherScreenUiEvent>(
    initialState = EditVoucherScreenUiState(),
    exceptionHandler = { syntax, exception ->
        exceptionHandler.onEditVoucherError(syntax, exception)
    }
) {

    override fun onEvent(event: EditVoucherScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            is EditVoucherScreenUiEvent.Initialize -> loadVoucher(event.voucherId)
            EditVoucherScreenUiEvent.SaveVoucher -> saveVoucher()
            is EditVoucherScreenUiEvent.CodeChanged -> updateCode(event.code)
            is EditVoucherScreenUiEvent.NameChanged -> updateName(event.name)
            is EditVoucherScreenUiEvent.DescriptionChanged -> updateDescription(event.description)
            is EditVoucherScreenUiEvent.TypeChanged -> updateType(event.type)
            is EditVoucherScreenUiEvent.AmountChanged -> updateAmount(event.amount)
            is EditVoucherScreenUiEvent.CurrencyChanged -> updateCurrency(event.currency)
            is EditVoucherScreenUiEvent.MaxClaimChanged -> updateMaxClaim(event.maxClaim)
            is EditVoucherScreenUiEvent.MaxUsageChanged -> updateMaxUsage(event.maxUsage)
            is EditVoucherScreenUiEvent.MinOrderItemChanged -> updateMinOrderItem(event.minOrderItem)
            is EditVoucherScreenUiEvent.MinOrderAmountChanged -> updateMinOrderAmount(event.minOrderAmount)
            is EditVoucherScreenUiEvent.MaxDiscountAmountChanged -> updateMaxDiscountAmount(event.maxDiscountAmount)
            is EditVoucherScreenUiEvent.ClaimPeriodStartChanged -> updateClaimPeriodStart(event.date)
            is EditVoucherScreenUiEvent.ClaimPeriodEndChanged -> updateClaimPeriodEnd(event.date)
            is EditVoucherScreenUiEvent.ImageUrlChanged -> updateImageUrl(event.imageUrl)
            is EditVoucherScreenUiEvent.IsActiveChanged -> updateIsActive(event.isActive)
            EditVoucherScreenUiEvent.ToggleConditionsSection -> toggleConditionsSection()
            else -> Unit
        }
    }

    private fun loadVoucher(voucherId: String) = intent {
        reduce { state.copy(isLoadingVoucher = true) }

        val voucher = repository.getVoucherDetail(voucherId)

        reduce {
            state.copy(
                isLoadingVoucher = false,
                voucher = voucher,
                code = TextFieldValue(voucher.code),
                name = TextFieldValue(voucher.name),
                description = TextFieldValue(voucher.description),
                type = voucher.type,
                amount = TextFieldValue(voucher.amount.toString()),
                currency = voucher.currency,
                maxClaim = TextFieldValue(voucher.conditions.maxClaim.toString()),
                maxUsage = TextFieldValue(voucher.conditions.maxUsage.toString()),
                minOrderItem = TextFieldValue(voucher.conditions.minOrderItem.toString()),
                minOrderAmount = TextFieldValue(voucher.conditions.minOrderAmount.toString()),
                maxDiscountAmount = TextFieldValue(voucher.conditions.maxDiscountAmount.toString()),
                claimPeriodStart = voucher.claimPeriodStart,
                claimPeriodEnd = voucher.claimPeriodEnd,
                imageUrl = voucher.imageUrl,
                isActive = voucher.isActive
            )
        }
    }

    private fun updateCode(code: TextFieldValue) = intent {
        reduce {
            state.copy(
                code = code,
                codeError = null
            )
        }
    }

    private fun updateName(name: TextFieldValue) = intent {
        reduce {
            state.copy(
                name = name,
                nameError = null
            )
        }
    }

    private fun updateDescription(description: TextFieldValue) = intent {
        reduce {
            state.copy(
                description = description,
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

    private fun updateCurrency(currency: VoucherDomain.Currency) = intent {
        reduce { state.copy(currency = currency) }
    }

    private fun updateMaxClaim(maxClaim: TextFieldValue) = intent {
        reduce {
            state.copy(
                maxClaim = maxClaim,
                maxClaimError = null
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

    private fun updateMaxDiscountAmount(maxDiscountAmount: TextFieldValue) = intent {
        reduce {
            state.copy(
                maxDiscountAmount = maxDiscountAmount,
                maxDiscountAmountError = null
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

    private fun updateIsActive(isActive: Boolean) = intent {
        reduce { state.copy(isActive = isActive) }
    }

    private fun toggleConditionsSection() = intent {
        reduce { state.copy(showConditionsSection = !state.showConditionsSection) }
    }

    private fun saveVoucher() = intent {
        val voucher = state.voucher ?: return@intent
        if (!validateForm(state)) return@intent

        reduce { state.copy(isLoading = true) }

        val conditions = VoucherConditions(
            maxClaim = state.maxClaim.text.toIntOrNull() ?: 0,
            maxUsage = state.maxUsage.text.toIntOrNull() ?: 0,
            minOrderItem = state.minOrderItem.text.toIntOrNull() ?: 0,
            minOrderAmount = state.minOrderAmount.text.toDoubleOrNull() ?: 0.0,
            maxDiscountAmount = state.maxDiscountAmount.text.toDoubleOrNull() ?: 0.0
        )

        repository.updateVoucher(
            id = voucher.id,
            code = state.code.text,
            name = state.name.text,
            description = state.description.text,
            type = state.type,
            amount = state.amount.text.toDoubleOrNull() ?: 0.0,
            currency = state.currency,
            conditions = conditions,
            claimPeriodStart = state.claimPeriodStart,
            claimPeriodEnd = state.claimPeriodEnd,
            imageUrl = state.imageUrl,
            isActive = state.isActive
        )

        reduce { state.copy(isLoading = false) }
        postSideEffect(EditVoucherScreenUiEvent.NavigateBack)
    }

    private fun validateForm(state: EditVoucherScreenUiState): Boolean {
        var hasError = false
        val newState = state.copy(
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
            claimPeriodEndError = null
        )

        val updatedState = when {
            state.code.text.isBlank() -> {
                hasError = true
                newState.copy(codeError = "Code is required")
            }

            state.name.text.isBlank() -> {
                hasError = true
                newState.copy(nameError = "Name is required")
            }

            state.description.text.isBlank() -> {
                hasError = true
                newState.copy(descriptionError = "Description is required")
            }

            state.amount.text.isBlank() || state.amount.text.toDoubleOrNull() == null -> {
                hasError = true
                newState.copy(amountError = "Valid amount is required")
            }

            state.maxClaim.text.isBlank() || state.maxClaim.text.toIntOrNull() == null -> {
                hasError = true
                newState.copy(maxClaimError = "Valid max claim is required")
            }

            state.maxUsage.text.isBlank() || state.maxUsage.text.toIntOrNull() == null -> {
                hasError = true
                newState.copy(maxUsageError = "Valid max usage is required")
            }

            state.minOrderItem.text.isBlank() || state.minOrderItem.text.toIntOrNull() == null -> {
                hasError = true
                newState.copy(minOrderItemError = "Valid min order item is required")
            }

            state.minOrderAmount.text.isBlank() || state.minOrderAmount.text.toDoubleOrNull() == null -> {
                hasError = true
                newState.copy(minOrderAmountError = "Valid min order amount is required")
            }

            state.maxDiscountAmount.text.isBlank() || state.maxDiscountAmount.text.toDoubleOrNull() == null -> {
                hasError = true
                newState.copy(maxDiscountAmountError = "Valid max discount amount is required")
            }

            state.claimPeriodStart.isBlank() -> {
                hasError = true
                newState.copy(claimPeriodStartError = "Start date is required")
            }

            state.claimPeriodEnd.isBlank() -> {
                hasError = true
                newState.copy(claimPeriodEndError = "End date is required")
            }

            else -> newState
        }

        return !hasError
    }
}
