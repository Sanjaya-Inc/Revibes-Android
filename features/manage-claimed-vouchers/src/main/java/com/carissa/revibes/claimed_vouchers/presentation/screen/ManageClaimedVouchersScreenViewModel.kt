package com.carissa.revibes.claimed_vouchers.presentation.screen

import com.carissa.revibes.claimed_vouchers.data.ManageClaimedVouchersRepository
import com.carissa.revibes.claimed_vouchers.domain.model.ClaimedVoucherDomain
import com.carissa.revibes.claimed_vouchers.presentation.handler.ManageClaimedVouchersExceptionHandler
import com.carissa.revibes.core.presentation.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.android.annotation.KoinViewModel

sealed interface ManageClaimedVouchersScreenUiEvent {
    data class OnSearchQueryChanged(val query: String) : ManageClaimedVouchersScreenUiEvent
    data object RefreshClaimedVouchers : ManageClaimedVouchersScreenUiEvent
    data object LoadClaimedVouchers : ManageClaimedVouchersScreenUiEvent
    data class OnLoadClaimedVouchersFailed(val message: String) : ManageClaimedVouchersScreenUiEvent
}

data class ManageClaimedVouchersScreenUiState(
    val claimedVouchers: ImmutableList<ClaimedVoucherDomain> = persistentListOf(),
    val filteredClaimedVouchers: ImmutableList<ClaimedVoucherDomain> = persistentListOf(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@KoinViewModel
class ManageClaimedVouchersScreenViewModel internal constructor(
    private val repository: ManageClaimedVouchersRepository,
    private val exceptionHandler: ManageClaimedVouchersExceptionHandler
) : BaseViewModel<ManageClaimedVouchersScreenUiState, ManageClaimedVouchersScreenUiEvent>(
    initialState = ManageClaimedVouchersScreenUiState(),
    onCreate = {
        onEvent(ManageClaimedVouchersScreenUiEvent.LoadClaimedVouchers)
    },
    exceptionHandler = { syntax, throwable ->
        exceptionHandler.onManageClaimedVouchersError(syntax, throwable)
    }
) {
    override fun onEvent(event: ManageClaimedVouchersScreenUiEvent) {
        when (event) {
            is ManageClaimedVouchersScreenUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.query)
            is ManageClaimedVouchersScreenUiEvent.RefreshClaimedVouchers,
            is ManageClaimedVouchersScreenUiEvent.LoadClaimedVouchers -> loadClaimedVouchers()
            else -> super.onEvent(event)
        }
    }

    private fun onSearchQueryChanged(query: String) = intent {
        reduce {
            state.copy(
                searchQuery = query,
                filteredClaimedVouchers = filterClaimedVouchers(state.claimedVouchers, query)
            )
        }
    }

    private fun loadClaimedVouchers() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        val claimedVouchers = repository.getClaimedVouchers()

        reduce {
            state.copy(
                claimedVouchers = claimedVouchers.toImmutableList(),
                filteredClaimedVouchers = filterClaimedVouchers(
                    claimedVouchers = claimedVouchers.toImmutableList(),
                    query = state.searchQuery
                ),
                isLoading = false,
                error = null
            )
        }
    }

    private fun filterClaimedVouchers(
        claimedVouchers: ImmutableList<ClaimedVoucherDomain>,
        query: String
    ): ImmutableList<ClaimedVoucherDomain> {
        if (query.isBlank()) return claimedVouchers

        return claimedVouchers.filter { voucher ->
            voucher.id.contains(query, ignoreCase = true) ||
                voucher.voucherCode?.contains(query, ignoreCase = true) == true ||
                voucher.items.any { item ->
                    item.metadata.name.contains(query, ignoreCase = true) ||
                        item.metadata.code.contains(query, ignoreCase = true)
                }
        }.toImmutableList()
    }
}
