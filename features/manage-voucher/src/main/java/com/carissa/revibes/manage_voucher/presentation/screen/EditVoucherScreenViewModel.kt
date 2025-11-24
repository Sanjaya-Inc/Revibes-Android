package com.carissa.revibes.manage_voucher.presentation.screen

import android.util.Log
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class EditVoucherScreenUiState(
    val isLoading: Boolean = false,
    val voucherId: String = "",
    val code: TextFieldValue = TextFieldValue(),
    val name: TextFieldValue = TextFieldValue(),
    val description: TextFieldValue = TextFieldValue(),
    val type: VoucherDomain.VoucherType = VoucherDomain.VoucherType.PERCENT_OFF,
    val amount: TextFieldValue = TextFieldValue(),
    val maxClaim: TextFieldValue = TextFieldValue(),
    val maxUsage: TextFieldValue = TextFieldValue(),
    val minOrderItem: TextFieldValue = TextFieldValue(),
    val minOrderAmount: TextFieldValue = TextFieldValue(),
    val maxDiscountAmount: TextFieldValue = TextFieldValue(),
    val claimPeriodStart: String = "",
    val claimPeriodEnd: String = "",
    val imageUrl: String? = null,
    val showConditionsSection: Boolean = true,
    val termConditions: ImmutableList<String> = persistentListOf(),
    val guides: ImmutableList<String> = persistentListOf(),
    val showTermConditionsSection: Boolean = true,
    val showGuidesSection: Boolean = true,
    val isInClaimPeriod: Boolean = false,
    val nameError: String? = null,
    val descriptionError: String? = null,
    val codeError: String? = null,
    val amountError: String? = null,
    val maxClaimError: String? = null,
    val maxUsageError: String? = null,
    val minOrderItemError: String? = null,
    val minOrderAmountError: String? = null,
    val maxDiscountAmountError: String? = null,
    val claimPeriodStartError: String? = null,
    val claimPeriodEndError: String? = null,
    val termConditionsError: String? = null,
    val guidesError: String? = null
)

sealed interface EditVoucherScreenUiEvent {
    data object OnVoucherUpdatedSuccessfully : EditVoucherScreenUiEvent
    data object SaveVoucher : EditVoucherScreenUiEvent
    data class Initialize(val voucher: VoucherDomain) : EditVoucherScreenUiEvent
    data class NameChanged(val name: TextFieldValue) : EditVoucherScreenUiEvent
    data class DescriptionChanged(val description: TextFieldValue) : EditVoucherScreenUiEvent
    data class CodeChanged(val code: TextFieldValue) : EditVoucherScreenUiEvent
    data class MaxClaimChanged(val maxClaim: TextFieldValue) : EditVoucherScreenUiEvent
    data class MaxUsageChanged(val maxUsage: TextFieldValue) : EditVoucherScreenUiEvent
    data class MinOrderItemChanged(val minOrderItem: TextFieldValue) : EditVoucherScreenUiEvent
    data class MinOrderAmountChanged(val minOrderAmount: TextFieldValue) : EditVoucherScreenUiEvent
    data class MaxDiscountAmountChanged(val maxDiscountAmount: TextFieldValue) : EditVoucherScreenUiEvent
    data class ClaimPeriodStartChanged(val date: String) : EditVoucherScreenUiEvent
    data class ClaimPeriodEndChanged(val date: String) : EditVoucherScreenUiEvent
    data object ToggleConditionsSection : EditVoucherScreenUiEvent
    data object ToggleTermConditionsSection : EditVoucherScreenUiEvent
    data object ToggleGuidesSection : EditVoucherScreenUiEvent
    data class AddTermCondition(val condition: String) : EditVoucherScreenUiEvent
    data class RemoveTermCondition(val index: Int) : EditVoucherScreenUiEvent
    data class UpdateTermCondition(val index: Int, val condition: String) : EditVoucherScreenUiEvent
    data class AddGuide(val guide: String) : EditVoucherScreenUiEvent
    data class RemoveGuide(val index: Int) : EditVoucherScreenUiEvent
    data class UpdateGuide(val index: Int, val guide: String) : EditVoucherScreenUiEvent
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
            is EditVoucherScreenUiEvent.Initialize -> loadVoucher(event.voucher)
            EditVoucherScreenUiEvent.SaveVoucher -> saveVoucher()
            is EditVoucherScreenUiEvent.NameChanged -> updateName(event.name)
            is EditVoucherScreenUiEvent.DescriptionChanged -> updateDescription(event.description)
            is EditVoucherScreenUiEvent.CodeChanged -> updateCode(event.code)
            is EditVoucherScreenUiEvent.MaxClaimChanged -> updateMaxClaim(event.maxClaim)
            is EditVoucherScreenUiEvent.MaxUsageChanged -> updateMaxUsage(event.maxUsage)
            is EditVoucherScreenUiEvent.MinOrderItemChanged -> updateMinOrderItem(event.minOrderItem)
            is EditVoucherScreenUiEvent.MinOrderAmountChanged -> updateMinOrderAmount(event.minOrderAmount)
            is EditVoucherScreenUiEvent.MaxDiscountAmountChanged -> updateMaxDiscountAmount(event.maxDiscountAmount)
            is EditVoucherScreenUiEvent.ClaimPeriodStartChanged -> updateClaimPeriodStart(event.date)
            is EditVoucherScreenUiEvent.ClaimPeriodEndChanged -> updateClaimPeriodEnd(event.date)
            EditVoucherScreenUiEvent.ToggleConditionsSection -> toggleConditionsSection()
            EditVoucherScreenUiEvent.ToggleTermConditionsSection -> toggleTermConditionsSection()
            EditVoucherScreenUiEvent.ToggleGuidesSection -> toggleGuidesSection()
            is EditVoucherScreenUiEvent.AddTermCondition -> addTermCondition(event.condition)
            is EditVoucherScreenUiEvent.RemoveTermCondition -> removeTermCondition(event.index)
            is EditVoucherScreenUiEvent.UpdateTermCondition -> updateTermCondition(event.index, event.condition)
            is EditVoucherScreenUiEvent.AddGuide -> addGuide(event.guide)
            is EditVoucherScreenUiEvent.RemoveGuide -> removeGuide(event.index)
            is EditVoucherScreenUiEvent.UpdateGuide -> updateGuide(event.index, event.guide)
            else -> Unit
        }
    }

    private fun loadVoucher(voucher: VoucherDomain) = intent {
        val isInClaimPeriod = checkIfInClaimPeriod(voucher.claimPeriodStart)

        reduce {
            state.copy(
                voucherId = voucher.id,
                code = TextFieldValue(voucher.code),
                name = TextFieldValue(voucher.name),
                description = TextFieldValue(voucher.description),
                type = voucher.value.type,
                amount = TextFieldValue(voucher.value.amount.toString()),
                maxClaim = TextFieldValue(voucher.conditions?.maxClaim?.toString() ?: ""),
                maxUsage = TextFieldValue(voucher.conditions?.maxUsage?.toString() ?: ""),
                minOrderItem = TextFieldValue(voucher.conditions?.minOrderItem?.toString() ?: ""),
                minOrderAmount = TextFieldValue(voucher.conditions?.minOrderAmount?.toString() ?: ""),
                maxDiscountAmount = TextFieldValue(voucher.conditions?.maxDiscountAmount?.toString() ?: ""),
                claimPeriodStart = voucher.claimPeriodStart,
                claimPeriodEnd = voucher.claimPeriodEnd,
                imageUrl = voucher.imageUri,
                termConditions = voucher.termConditions.toImmutableList(),
                guides = voucher.guides.toImmutableList(),
                isInClaimPeriod = isInClaimPeriod
            )
        }
    }

    private fun checkIfInClaimPeriod(claimPeriodStart: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ISO_DATE
            val startDate = LocalDate.parse(claimPeriodStart, formatter)
            val currentDate = LocalDate.now()
            currentDate >= startDate
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing claim period start date", e)
            false
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

    private fun updateCode(code: TextFieldValue) = intent {
        if (!state.isInClaimPeriod) {
            val formattedCode = code.text.uppercase().replace(" ", "-")
            reduce {
                state.copy(
                    code = code.copy(text = formattedCode),
                    codeError = null
                )
            }
        }
    }

    private fun updateMaxClaim(maxClaim: TextFieldValue) = intent {
        if (!state.isInClaimPeriod) {
            reduce {
                state.copy(
                    maxClaim = maxClaim,
                    maxClaimError = null
                )
            }
        }
    }

    private fun updateMaxDiscountAmount(maxDiscountAmount: TextFieldValue) = intent {
        if (!state.isInClaimPeriod) {
            reduce {
                state.copy(
                    maxDiscountAmount = maxDiscountAmount,
                    maxDiscountAmountError = null
                )
            }
        }
    }

    private fun updateMaxUsage(maxUsage: TextFieldValue) = intent {
        if (!state.isInClaimPeriod) {
            reduce {
                state.copy(
                    maxUsage = maxUsage,
                    maxUsageError = null
                )
            }
        }
    }

    private fun updateMinOrderItem(minOrderItem: TextFieldValue) = intent {
        if (!state.isInClaimPeriod) {
            reduce {
                state.copy(
                    minOrderItem = minOrderItem,
                    minOrderItemError = null
                )
            }
        }
    }

    private fun updateMinOrderAmount(minOrderAmount: TextFieldValue) = intent {
        if (!state.isInClaimPeriod) {
            reduce {
                state.copy(
                    minOrderAmount = minOrderAmount,
                    minOrderAmountError = null
                )
            }
        }
    }

    private fun updateClaimPeriodStart(date: String) = intent {
        if (!state.isInClaimPeriod) {
            reduce {
                state.copy(
                    claimPeriodStart = date,
                    claimPeriodStartError = null
                )
            }
        }
    }

    private fun updateClaimPeriodEnd(date: String) = intent {
        if (!state.isInClaimPeriod) {
            reduce {
                state.copy(
                    claimPeriodEnd = date,
                    claimPeriodEndError = null
                )
            }
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
        if (!state.isInClaimPeriod && condition.isNotBlank()) {
            reduce {
                state.copy(
                    termConditions = (state.termConditions + condition).toImmutableList(),
                    termConditionsError = null
                )
            }
        }
    }

    private fun removeTermCondition(index: Int) = intent {
        if (!state.isInClaimPeriod && index in state.termConditions.indices) {
            reduce {
                state.copy(
                    termConditions = state.termConditions.toMutableList().apply { removeAt(index) }.toImmutableList()
                )
            }
        }
    }

    private fun updateTermCondition(index: Int, condition: String) = intent {
        if (!state.isInClaimPeriod && index in state.termConditions.indices) {
            reduce {
                state.copy(
                    termConditions = state.termConditions.toMutableList().apply {
                        set(index, condition)
                    }.toImmutableList(),
                    termConditionsError = null
                )
            }
        }
    }

    private fun addGuide(guide: String) = intent {
        if (!state.isInClaimPeriod && guide.isNotBlank()) {
            reduce {
                state.copy(
                    guides = (state.guides + guide).toImmutableList(),
                    guidesError = null
                )
            }
        }
    }

    private fun removeGuide(index: Int) = intent {
        if (!state.isInClaimPeriod && index in state.guides.indices) {
            reduce {
                state.copy(guides = state.guides.toMutableList().apply { removeAt(index) }.toImmutableList())
            }
        }
    }

    private fun updateGuide(index: Int, guide: String) = intent {
        if (!state.isInClaimPeriod && index in state.guides.indices) {
            reduce {
                state.copy(
                    guides = state.guides.toMutableList().apply { set(index, guide) }.toImmutableList(),
                    guidesError = null
                )
            }
        }
    }

    private fun saveVoucher() = intent {
        val isFormValid = validateForm(state)
        if (isFormValid) {
            reduce { state.copy(isLoading = true) }

            try {
                val code = if (!state.isInClaimPeriod) state.code.text else null
                val conditions = if (!state.isInClaimPeriod) {
                    VoucherConditions(
                        maxClaim = state.maxClaim.text.toIntOrNull() ?: 0,
                        maxUsage = state.maxUsage.text.toIntOrNull() ?: 0,
                        minOrderItem = state.minOrderItem.text.toIntOrNull() ?: 0,
                        minOrderAmount = state.minOrderAmount.text.toLongOrNull() ?: 0L,
                        maxDiscountAmount = state.maxDiscountAmount.text.toLongOrNull() ?: 0L
                    )
                } else {
                    null
                }
                val claimPeriodStart = if (!state.isInClaimPeriod) state.claimPeriodStart else null
                val claimPeriodEnd = if (!state.isInClaimPeriod) state.claimPeriodEnd else null
                val termConditions = if (!state.isInClaimPeriod) state.termConditions else null
                val guides = if (!state.isInClaimPeriod) state.guides else null

                repository.updateVoucher(
                    id = state.voucherId,
                    name = state.name.text,
                    description = state.description.text,
                    code = code,
                    conditions = conditions,
                    claimPeriodStart = claimPeriodStart,
                    claimPeriodEnd = claimPeriodEnd,
                    termConditions = termConditions,
                    guides = guides
                )

                reduce { state.copy(isLoading = false) }
                postSideEffect(EditVoucherScreenUiEvent.OnVoucherUpdatedSuccessfully)
            } catch (e: Exception) {
                reduce { state.copy(isLoading = false) }
                postSideEffect(EditVoucherScreenUiEvent.OnUpdateVoucherFailed(e.message ?: "Update failed"))
            }
        } else {
            Log.e(TAG, "Form validation failed")
        }
    }

    private fun validateForm(state: EditVoucherScreenUiState): Boolean {
        var hasError = false
        var newState = state.copy(
            nameError = null,
            descriptionError = null,
            codeError = null,
            maxClaimError = null,
            maxUsageError = null,
            minOrderItemError = null,
            minOrderAmountError = null,
            maxDiscountAmountError = null,
            claimPeriodStartError = null,
            claimPeriodEndError = null,
            termConditionsError = null,
            guidesError = null
        )

        if (state.name.text.isBlank()) {
            hasError = true
            newState = newState.copy(nameError = "Name is required")
        }

        if (state.description.text.isBlank()) {
            hasError = true
            newState = newState.copy(descriptionError = "Description is required")
        }

        intent { reduce { newState } }

        return !hasError
    }

    companion object {
        private const val TAG = "EditVoucherScreenViewModel"
    }
}
