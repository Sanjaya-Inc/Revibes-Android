package com.carissa.revibes.exchange_points.presentation.screen

import androidx.lifecycle.viewModelScope
import com.carissa.revibes.core.data.user.local.UserDataSourceGetter
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.model.UserPointFlow
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.data.ExchangePointsRepository
import com.carissa.revibes.exchange_points.data.model.PurchaseItem
import com.carissa.revibes.exchange_points.data.model.PurchaseRequest
import com.carissa.revibes.exchange_points.domain.model.Voucher
import com.carissa.revibes.exchange_points.presentation.handler.ExchangePointsExceptionHandler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

data class ExchangePointDetailScreenUiState(
    val isLoading: Boolean = false,
    val discount: Int = 70,
    val couponId: String = "7985x17228",
    val title: String = "DISCOUNT 70%",
    val validUntil: String = "Valid until 31 December 2024",
    val description: String = "We are here for bigger offers. Especially for those " +
        "of you shopping fans, " +
        "get a 70% promo for every item worth a minimum of IDR 300 thousand at Revibes Store",
    val image: String = "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    val showBottomSheet: Boolean = false,
    val quantity: Int = 1,
)

sealed interface ExchangePointDetailScreenUiEvent {
    data object NavigateToProfile : ExchangePointDetailScreenUiEvent, NavigationEvent

    data class NavigateToConfirmation(
        val voucherId: String,
        val voucherName: String,
        val voucherImage: String,
        val voucherValidUntil: String,
        val voucherPrice: Int,
        val quantity: Int
    ) : ExchangePointDetailScreenUiEvent, NavigationEvent

    data class PurchaseVoucher(
        val id: String,
        val qty: Int
    ) : ExchangePointDetailScreenUiEvent

    data class ShowToast(val message: String) : ExchangePointDetailScreenUiEvent
}

@KoinViewModel
class ExchangePointDetailScreenViewModel(
    private val repository: ExchangePointsRepository,
    private val userPointFlow: UserPointFlow,
    private val userDataSourceGetter: UserDataSourceGetter,
    private val exceptionHandler: ExchangePointsExceptionHandler
) : BaseViewModel<ExchangePointDetailScreenUiState, ExchangePointDetailScreenUiEvent>(
    initialState = ExchangePointDetailScreenUiState(),
    exceptionHandler = { syntax, exception ->
        exceptionHandler.onExchangePointsDetailError(syntax, exception)
    }
) {
    val coins = userPointFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), 0)

    private var currentVoucher: Voucher? = null

    fun setVoucher(voucher: Voucher) {
        currentVoucher = voucher
    }

    override fun onEvent(event: ExchangePointDetailScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            is ExchangePointDetailScreenUiEvent.PurchaseVoucher -> purchaseVoucher(
                event.id,
                event.qty
            )

            else -> Unit
        }
    }

    private fun purchaseVoucher(id: String, qty: Int) {
        intent {
            reduce { state.copy(isLoading = true) }
            val isVerified = userDataSourceGetter.getUserValue().getOrNull()?.verified == true
            if (!isVerified) {
                postSideEffect(
                    ExchangePointDetailScreenUiEvent.ShowToast(
                        "Please verify your account to admin first!"
                    )
                )
                reduce { state.copy(isLoading = false) }
                return@intent
            }
            val purchaseRequest = PurchaseRequest(
                items = listOf(
                    PurchaseItem(
                        id = id,
                        qty = qty
                    )
                ),
                paymentMethod = "point",
                currency = "revibe-point"
            )
            repository.purchaseVoucher(purchaseRequest)

            reduce { state.copy(isLoading = false) }
            userPointFlow.update()
            postSideEffect(ExchangePointDetailScreenUiEvent.ShowToast("Voucher purchased successfully"))

            currentVoucher?.let { voucher ->
                onEvent(
                    ExchangePointDetailScreenUiEvent.NavigateToConfirmation(
                        voucherId = voucher.id,
                        voucherName = voucher.name,
                        voucherImage = voucher.imageUri,
                        voucherValidUntil = voucher.validUntil,
                        voucherPrice = voucher.point,
                        quantity = qty
                    )
                )
            }
        }
    }
}
