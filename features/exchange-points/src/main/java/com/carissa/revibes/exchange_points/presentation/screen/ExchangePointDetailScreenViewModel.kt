/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.exchange_points.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import org.koin.android.annotation.KoinViewModel

data class ExchangePointDetailScreenUiState(
    val isLoading: Boolean = false,
    val discount: Int = 70,
    val couponId: String = "7985x17228",
    val title: String = "DISCOUNT 70%",
    val validUntil: String = "Valid until 31 December 2024",
    val description: String = "We are here for bigger offers. Especially for those of you shopping fans, " +
        "get a 70% promo for every item worth a minimum of IDR 300 thousand at Revibes Store",
    val image: String = "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    val terms: List<String> = listOf(
        "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut " +
            "laoreet dolore magna aliquam erat volutpat.",
        "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut " +
            "aliquip ex ea commodo consequat.",
        "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum " +
            "dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit " +
            "praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.",
        "Lorem ipsum dolor sit amet, cons ectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut " +
            "laoreet dolore magna aliquam erat volutpat.",
        "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut " +
            "aliquip ex ea commodo consequat.",
        "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut " +
            "laoreet dolore magna aliquam erat volutpat.",
        "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut " +
            "aliquip ex ea commodo consequat.",
        "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum " +
            "dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit " +
            "praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi."
    ),
    val showBottomSheet: Boolean = false,
    val quantity: Int = 1
)

sealed interface ExchangePointDetailScreenUiEvent {
    data object NavigateToProfile : ExchangePointDetailScreenUiEvent, NavigationEvent
    data object NavigateToConfirmation : ExchangePointDetailScreenUiEvent, NavigationEvent
    data object BuyCoupon : ExchangePointDetailScreenUiEvent
    data object DismissBottomSheet : ExchangePointDetailScreenUiEvent
    data object IncreaseQuantity : ExchangePointDetailScreenUiEvent
    data object DecreaseQuantity : ExchangePointDetailScreenUiEvent
    data object ConfirmPurchase : ExchangePointDetailScreenUiEvent
}

@KoinViewModel
class ExchangePointDetailScreenViewModel : BaseViewModel<
    ExchangePointDetailScreenUiState,
    ExchangePointDetailScreenUiEvent
    >(
    ExchangePointDetailScreenUiState()
) {
    override fun onEvent(event: ExchangePointDetailScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ExchangePointDetailScreenUiEvent.BuyCoupon -> {
                intent {
                    reduce { state.copy(showBottomSheet = true) }
                }
            }

            ExchangePointDetailScreenUiEvent.DismissBottomSheet -> {
                intent {
                    reduce { state.copy(showBottomSheet = false) }
                }
            }

            ExchangePointDetailScreenUiEvent.IncreaseQuantity -> {
                intent {
                    reduce { state.copy(quantity = state.quantity + 1) }
                }
            }

            ExchangePointDetailScreenUiEvent.DecreaseQuantity -> {
                intent {
                    if (state.quantity > 1) {
                        reduce { state.copy(quantity = state.quantity - 1) }
                    }
                }
            }

            ExchangePointDetailScreenUiEvent.ConfirmPurchase -> {
                intent {
                    reduce { state.copy(showBottomSheet = false) }
                    // Handle purchase confirmation logic here
                    onEvent(ExchangePointDetailScreenUiEvent.NavigateToConfirmation)
                }
            }
            else -> Unit
        }
    }
}
