package com.carissa.revibes.exchange_points.presentation.screen

import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.data.ExchangePointsRepository
import com.carissa.revibes.exchange_points.domain.model.Voucher
import com.carissa.revibes.exchange_points.presentation.handler.ExchangePointsExceptionHandler
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.koin.android.annotation.KoinViewModel

data class ExchangePointsScreenUiState(
    val isLoading: Boolean = false,
    val vouchers: PersistentList<Voucher> = persistentListOf(),
    val points: Int = 0,
    val error: String? = null,
)

sealed interface ExchangePointsScreenUiEvent {
    data class NavigateToDetailExchangePoint(val voucherId: String) :
        NavigationEvent,
        ExchangePointsScreenUiEvent

    data object NavigateToProfile : ExchangePointsScreenUiEvent, NavigationEvent
    data object Initialize : ExchangePointsScreenUiEvent
    data object Refresh : ExchangePointsScreenUiEvent
    data class OnLoadVouchersFailed(val message: String) : ExchangePointsScreenUiEvent
}

@KoinViewModel
class ExchangePointsScreenViewModel(
    private val repository: ExchangePointsRepository,
    userDataSource: UserDataSource,
    private val exceptionHandler: ExchangePointsExceptionHandler
) : BaseViewModel<ExchangePointsScreenUiState, ExchangePointsScreenUiEvent>(
    initialState = ExchangePointsScreenUiState(
        points = userDataSource.getUserValue().getOrNull()?.coins ?: 0
    ),
    onCreate = { onEvent(ExchangePointsScreenUiEvent.Initialize) },
    exceptionHandler = { syntax, exception ->
        exceptionHandler.onExchangePointsError(syntax, exception)
    }
) {

    override fun onEvent(event: ExchangePointsScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ExchangePointsScreenUiEvent.Initialize -> loadVouchers()
            ExchangePointsScreenUiEvent.Refresh -> loadVouchers(true)
            else -> Unit
        }
    }

    private fun loadVouchers(refresh: Boolean = false) = intent {
        if (refresh) {
            reduce { state.copy(isLoading = true, error = null) }
        } else if (state.vouchers.isEmpty()) {
            reduce { state.copy(isLoading = true, error = null) }
        }

        val vouchers = repository.getVouchers()
        reduce {
            state.copy(
                isLoading = false,
                vouchers = vouchers.toPersistentList(),
                error = null
            )
        }
    }
}
