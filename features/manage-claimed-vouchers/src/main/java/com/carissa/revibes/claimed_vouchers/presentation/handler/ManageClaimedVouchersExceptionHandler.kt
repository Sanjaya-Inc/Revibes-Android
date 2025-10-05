package com.carissa.revibes.claimed_vouchers.presentation.handler

import android.util.Log
import com.carissa.revibes.claimed_vouchers.presentation.screen.ManageClaimedVouchersScreenUiEvent
import com.carissa.revibes.claimed_vouchers.presentation.screen.ManageClaimedVouchersScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class ManageClaimedVouchersExceptionHandler {
    suspend fun onManageClaimedVouchersError(
        syntax: Syntax<ManageClaimedVouchersScreenUiState, ManageClaimedVouchersScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce {
            state.copy(
                isLoading = false,
                error = throwable.message ?: "Unknown error occurred"
            )
        }

        postSideEffect(
            ManageClaimedVouchersScreenUiEvent.OnLoadClaimedVouchersFailed(
                throwable.message ?: "Failed to load claimed vouchers"
            )
        )

        Log.e(TAG, "onManageClaimedVouchersError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "ManageClaimedVouchersExceptionHandler"
    }
}
