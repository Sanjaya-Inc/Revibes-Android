package com.carissa.revibes.manage_voucher.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.manage_voucher.data.ManageVoucherRepository
import com.carissa.revibes.manage_voucher.data.model.PaginationData
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import com.carissa.revibes.manage_voucher.presentation.handler.ManageVoucherExceptionHandler
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.koin.android.annotation.KoinViewModel

data class ManageVoucherScreenUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue(),
    val vouchers: PersistentList<VoucherDomain> = persistentListOf(),
    val filteredVouchers: PersistentList<VoucherDomain> = persistentListOf(),
    val pagination: PaginationData? = null,
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val voucherToDelete: VoucherDomain? = null,
    val showExchangeDialog: Boolean = false,
    val voucherToExchange: VoucherDomain? = null,
    val isCreatingExchange: Boolean = false
)

sealed interface ManageVoucherScreenUiEvent {
    data object NavigateToAddVoucher : NavigationEvent, ManageVoucherScreenUiEvent
    data class NavigateToEditVoucher(val voucher: VoucherDomain) : NavigationEvent, ManageVoucherScreenUiEvent
    data object Initialize : ManageVoucherScreenUiEvent
    data object LoadMore : ManageVoucherScreenUiEvent
    data object Refresh : ManageVoucherScreenUiEvent
    data class SearchValueChanged(val searchValue: TextFieldValue) : ManageVoucherScreenUiEvent
    data class ShowDeleteDialog(val voucher: VoucherDomain) : ManageVoucherScreenUiEvent
    data object HideDeleteDialog : ManageVoucherScreenUiEvent
    data object ConfirmDelete : ManageVoucherScreenUiEvent
    data class ToggleVoucherStatus(val voucher: VoucherDomain) : ManageVoucherScreenUiEvent
    data class ShowExchangeDialog(val voucher: VoucherDomain) : ManageVoucherScreenUiEvent
    data object HideExchangeDialog : ManageVoucherScreenUiEvent
    data class CreateExchange(
        val amount: Int,
        val description: String,
        val quota: Int,
        val endedAt: String?
    ) : ManageVoucherScreenUiEvent

    // Side effects
    data class OnToggleStatusSuccess(val message: String) : ManageVoucherScreenUiEvent
    data class OnToggleStatusFailed(val message: String) : ManageVoucherScreenUiEvent
    data class OnExchangeSuccess(val message: String) : ManageVoucherScreenUiEvent
    data class OnExchangeFailed(val message: String) : ManageVoucherScreenUiEvent
}

@KoinViewModel
class ManageVoucherScreenViewModel(
    private val repository: ManageVoucherRepository,
    private val exceptionHandler: ManageVoucherExceptionHandler,
    private val createExchangeVoucherUseCase:
    com.carissa.revibes.manage_voucher.domain.usecase.CreateExchangeVoucherUseCase
) : BaseViewModel<ManageVoucherScreenUiState, ManageVoucherScreenUiEvent>(
    initialState = ManageVoucherScreenUiState(),
    onCreate = { onEvent(ManageVoucherScreenUiEvent.Initialize) },
    exceptionHandler = { syntax, exception ->
        exceptionHandler.onManageVoucherError(syntax, exception)
    }
) {

    override fun onEvent(event: ManageVoucherScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            is ManageVoucherScreenUiEvent.Initialize -> loadVouchers()
            is ManageVoucherScreenUiEvent.LoadMore -> loadMoreVouchers()
            is ManageVoucherScreenUiEvent.Refresh -> onRefresh()
            is ManageVoucherScreenUiEvent.SearchValueChanged -> onSearchValueChanged(event.searchValue)
            is ManageVoucherScreenUiEvent.ShowDeleteDialog -> showDeleteDialog(event.voucher)
            is ManageVoucherScreenUiEvent.HideDeleteDialog -> hideDeleteDialog()
            is ManageVoucherScreenUiEvent.ConfirmDelete -> confirmDelete()
            is ManageVoucherScreenUiEvent.ToggleVoucherStatus -> toggleVoucherStatus(event.voucher)
            is ManageVoucherScreenUiEvent.ShowExchangeDialog -> showExchangeDialog(event.voucher)
            is ManageVoucherScreenUiEvent.HideExchangeDialog -> hideExchangeDialog()
            is ManageVoucherScreenUiEvent.CreateExchange -> createExchange(
                event.amount,
                event.description,
                event.quota,
                event.endedAt
            )
            else -> Unit
        }
    }

    private fun loadVouchers(refresh: Boolean = false) = intent {
        if (refresh) {
            reduce { state.copy(isLoading = true, error = null) }
        } else if (state.vouchers.isEmpty()) {
            reduce { state.copy(isLoading = true, error = null) }
        } else {
            reduce { state.copy(isLoadingMore = true, error = null) }
        }

        val lastDocId = if (refresh) null else state.pagination?.lastDocId

        val result = repository.getVoucherList(
            limit = 10,
            lastDocId = lastDocId,
            direction = "next"
        )

        val newVouchers = if (refresh) {
            result.vouchers.toPersistentList()
        } else {
            val existingIds = state.vouchers.map { it.id }.toSet()
            val uniqueNewVouchers = result.vouchers.filter { it.id !in existingIds }
            (state.vouchers + uniqueNewVouchers).toPersistentList()
        }

        reduce {
            state.copy(
                isLoading = false,
                isLoadingMore = false,
                vouchers = newVouchers,
                filteredVouchers = filterVouchers(newVouchers, state.searchValue.text),
                pagination = result.pagination,
                error = null
            )
        }
    }

    private fun loadMoreVouchers() = intent {
        if (state.pagination?.hasMoreNext == true && !state.isLoadingMore) {
            loadVouchers(refresh = false)
        }
    }

    private fun onSearchValueChanged(searchValue: TextFieldValue) = intent {
        val filteredVouchers = filterVouchers(state.vouchers, searchValue.text)
        reduce {
            state.copy(
                searchValue = searchValue,
                filteredVouchers = filteredVouchers
            )
        }
    }

    private fun onRefresh() {
        loadVouchers(refresh = true)
    }

    private fun showDeleteDialog(voucher: VoucherDomain) = intent {
        reduce {
            state.copy(
                showDeleteDialog = true,
                voucherToDelete = voucher
            )
        }
    }

    private fun hideDeleteDialog() = intent {
        reduce {
            state.copy(
                showDeleteDialog = false,
                voucherToDelete = null
            )
        }
    }

    private fun confirmDelete() = intent {
        val voucher = state.voucherToDelete ?: return@intent

        repository.deleteVoucher(voucher.id)

        val updatedVouchers = state.vouchers.filter { it.id != voucher.id }.toPersistentList()

        reduce {
            state.copy(
                vouchers = updatedVouchers,
                filteredVouchers = filterVouchers(updatedVouchers, state.searchValue.text),
                showDeleteDialog = false,
                voucherToDelete = null
            )
        }
    }

    private fun toggleVoucherStatus(voucher: VoucherDomain) = intent {
        val newStatus = !voucher.isAvailable

        val updatedVouchers = state.vouchers.map { v ->
            if (v.id == voucher.id) {
                v.copy(isAvailable = newStatus)
            } else {
                v
            }
        }.toPersistentList()

        reduce {
            state.copy(
                vouchers = updatedVouchers,
                filteredVouchers = filterVouchers(updatedVouchers, state.searchValue.text)
            )
        }

        try {
            repository.updateVoucherStatus(voucher.id, newStatus)

            val statusText = if (newStatus) "enabled" else "disabled"
            postSideEffect(
                ManageVoucherScreenUiEvent.OnToggleStatusSuccess(
                    "Voucher ${voucher.name} has been $statusText"
                )
            )
        } catch (e: Exception) {
            val revertedVouchers = state.vouchers.map { v ->
                if (v.id == voucher.id) {
                    v.copy(isAvailable = !newStatus)
                } else {
                    v
                }
            }.toPersistentList()

            reduce {
                state.copy(
                    vouchers = revertedVouchers,
                    filteredVouchers = filterVouchers(revertedVouchers, state.searchValue.text)
                )
            }

            postSideEffect(
                ManageVoucherScreenUiEvent.OnToggleStatusFailed(
                    "Failed to update voucher status: ${e.message.orEmpty()}"
                )
            )
        }
    }

    private fun showExchangeDialog(voucher: VoucherDomain) = intent {
        reduce {
            state.copy(
                showExchangeDialog = true,
                voucherToExchange = voucher
            )
        }
    }

    private fun hideExchangeDialog() = intent {
        reduce {
            state.copy(
                showExchangeDialog = false,
                voucherToExchange = null,
                isCreatingExchange = false
            )
        }
    }

    private fun createExchange(amount: Int, description: String, quota: Int, endedAt: String?) = intent {
        val voucher = state.voucherToExchange ?: return@intent

        reduce { state.copy(isCreatingExchange = true) }

        try {
            createExchangeVoucherUseCase(
                sourceId = voucher.id,
                amount = amount,
                description = description,
                quota = quota,
                endedAt = endedAt
            )

            reduce {
                state.copy(
                    showExchangeDialog = false,
                    voucherToExchange = null,
                    isCreatingExchange = false
                )
            }

            postSideEffect(
                ManageVoucherScreenUiEvent.OnExchangeSuccess(
                    "Exchange setup successfully for ${voucher.name}"
                )
            )
        } catch (e: Throwable) {
            reduce {
                state.copy(
                    isCreatingExchange = false
                )
            }

            postSideEffect(
                ManageVoucherScreenUiEvent.OnExchangeFailed(
                    "Failed to setup exchange: ${e.message.orEmpty()}"
                )
            )
        }
    }

    private fun filterVouchers(
        vouchers: PersistentList<VoucherDomain>,
        searchQuery: String
    ): PersistentList<VoucherDomain> {
        return if (searchQuery.isBlank()) {
            vouchers
        } else {
            vouchers.filter { voucher ->
                voucher.name.contains(searchQuery, ignoreCase = true) ||
                    voucher.code.contains(searchQuery, ignoreCase = true) ||
                    voucher.description.contains(searchQuery, ignoreCase = true) ||
                    voucher.value.type.name.contains(searchQuery, ignoreCase = true)
            }.toPersistentList()
        }
    }
}
