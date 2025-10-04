package com.carissa.revibes.exchange_points.presentation.screen

import androidx.lifecycle.viewModelScope
import com.carissa.revibes.core.data.main.remote.config.ConfigRepository
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.model.UserPointFlow
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.data.ExchangePointsRepository
import com.carissa.revibes.exchange_points.domain.model.Voucher
import com.carissa.revibes.exchange_points.presentation.handler.ExchangePointsExceptionHandler
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

data class ExchangePointsScreenUiState(
    val isLoading: Boolean = false,
    val isMaintenance: Boolean = false,
    val vouchers: PersistentList<Voucher> = persistentListOf(),
    val error: String? = null,
)

sealed interface ExchangePointsScreenUiEvent {
    data object NavigateBack : ExchangePointsScreenUiEvent
    data class NavigateToDetailExchangePoint(val voucher: Voucher) :
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
    userPointFlow: UserPointFlow,
    private val exceptionHandler: ExchangePointsExceptionHandler,
    configRepository: ConfigRepository
) : BaseViewModel<ExchangePointsScreenUiState, ExchangePointsScreenUiEvent>(
    initialState = ExchangePointsScreenUiState(
        isMaintenance = !configRepository.getExchangesFeatureFlagEnabled()
    ),
    onCreate = {
        if (!it.state.isMaintenance) {
            onEvent(ExchangePointsScreenUiEvent.Initialize)
        }
    },
    exceptionHandler = { syntax, exception ->
        exceptionHandler.onExchangePointsError(syntax, exception)
    }
) {

    val coins = userPointFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000L), 0)

    override fun onEvent(event: ExchangePointsScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ExchangePointsScreenUiEvent.NavigateBack -> intent {
                postSideEffect(event)
            }

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
