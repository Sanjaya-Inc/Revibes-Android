package com.carissa.revibes.manage_voucher.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.manage_voucher.data.ManageVoucherRepository
import com.carissa.revibes.manage_voucher.domain.model.VoucherConditions
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import com.carissa.revibes.manage_voucher.presentation.handler.ManageVoucherExceptionHandler
import org.koin.android.annotation.KoinViewModel

data class AddVoucherScreenUiState(
    val isLoading: Boolean = false,
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

sealed interface AddVoucherScreenUiEvent : NavigationEvent {
    data object NavigateBack : AddVoucherScreenUiEvent
    data object SaveVoucher : AddVoucherScreenUiEvent
    data class CodeChanged(val code: TextFieldValue) : AddVoucherScreenUiEvent
    data class NameChanged(val name: TextFieldValue) : AddVoucherScreenUiEvent
    data class DescriptionChanged(val description: TextFieldValue) : AddVoucherScreenUiEvent
    data class TypeChanged(val type: VoucherDomain.VoucherType) : AddVoucherScreenUiEvent
    data class AmountChanged(val amount: TextFieldValue) : AddVoucherScreenUiEvent
    data class CurrencyChanged(val currency: VoucherDomain.Currency) : AddVoucherScreenUiEvent
    data class MaxClaimChanged(val maxClaim: TextFieldValue) : AddVoucherScreenUiEvent
    data class MaxUsageChanged(val maxUsage: TextFieldValue) : AddVoucherScreenUiEvent
    data class MinOrderItemChanged(val minOrderItem: TextFieldValue) : AddVoucherScreenUiEvent
    data class MinOrderAmountChanged(val minOrderAmount: TextFieldValue) : AddVoucherScreenUiEvent
    data class MaxDiscountAmountChanged(val maxDiscountAmount: TextFieldValue) :
        AddVoucherScreenUiEvent

    data class ClaimPeriodStartChanged(val date: String) : AddVoucherScreenUiEvent
    data class ClaimPeriodEndChanged(val date: String) : AddVoucherScreenUiEvent
    data class ImageUrlChanged(val imageUrl: String?) : AddVoucherScreenUiEvent
    data object ToggleConditionsSection : AddVoucherScreenUiEvent
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
            is AddVoucherScreenUiEvent.CodeChanged -> updateCode(event.code)
            is AddVoucherScreenUiEvent.NameChanged -> updateName(event.name)
            is AddVoucherScreenUiEvent.DescriptionChanged -> updateDescription(event.description)
            is AddVoucherScreenUiEvent.TypeChanged -> updateType(event.type)
            is AddVoucherScreenUiEvent.AmountChanged -> updateAmount(event.amount)
            is AddVoucherScreenUiEvent.CurrencyChanged -> updateCurrency(event.currency)
            is AddVoucherScreenUiEvent.MaxClaimChanged -> updateMaxClaim(event.maxClaim)
            is AddVoucherScreenUiEvent.MaxUsageChanged -> updateMaxUsage(event.maxUsage)
            is AddVoucherScreenUiEvent.MinOrderItemChanged -> updateMinOrderItem(event.minOrderItem)
            is AddVoucherScreenUiEvent.MinOrderAmountChanged -> updateMinOrderAmount(event.minOrderAmount)
            is AddVoucherScreenUiEvent.MaxDiscountAmountChanged -> updateMaxDiscountAmount(event.maxDiscountAmount)
            is AddVoucherScreenUiEvent.ClaimPeriodStartChanged -> updateClaimPeriodStart(event.date)
            is AddVoucherScreenUiEvent.ClaimPeriodEndChanged -> updateClaimPeriodEnd(event.date)
            is AddVoucherScreenUiEvent.ImageUrlChanged -> updateImageUrl(event.imageUrl)
            AddVoucherScreenUiEvent.ToggleConditionsSection -> toggleConditionsSection()
            else -> Unit
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

    private fun toggleConditionsSection() = intent {
        reduce { state.copy(showConditionsSection = !state.showConditionsSection) }
    }

    private fun saveVoucher() = intent {
        if (!validateForm(state)) return@intent

        reduce { state.copy(isLoading = true) }

        val conditions = VoucherConditions(
            maxClaim = state.maxClaim.text.toIntOrNull() ?: 0,
            maxUsage = state.maxUsage.text.toIntOrNull() ?: 0,
            minOrderItem = state.minOrderItem.text.toIntOrNull() ?: 0,
            minOrderAmount = state.minOrderAmount.text.toDoubleOrNull() ?: 0.0,
            maxDiscountAmount = state.maxDiscountAmount.text.toDoubleOrNull() ?: 0.0
        )

        repository.createVoucher(
            code = state.code.text,
            name = state.name.text,
            description = state.description.text,
            type = state.type,
            amount = state.amount.text.toDoubleOrNull() ?: 0.0,
            currency = state.currency,
            conditions = conditions,
            claimPeriodStart = state.claimPeriodStart,
            claimPeriodEnd = state.claimPeriodEnd,
            imageUrl = state.imageUrl
        )

        reduce { state.copy(isLoading = false) }
        postSideEffect(AddVoucherScreenUiEvent.NavigateBack)
    }

    private fun validateForm(state: AddVoucherScreenUiState): Boolean {
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
