package com.carissa.revibes.manage_voucher.presentation.handler

import android.util.Log
import com.carissa.revibes.manage_voucher.presentation.screen.AddVoucherScreenUiEvent
import com.carissa.revibes.manage_voucher.presentation.screen.AddVoucherScreenUiState
import com.carissa.revibes.manage_voucher.presentation.screen.EditVoucherScreenUiEvent
import com.carissa.revibes.manage_voucher.presentation.screen.EditVoucherScreenUiState
import com.carissa.revibes.manage_voucher.presentation.screen.ManageVoucherScreenUiEvent
import com.carissa.revibes.manage_voucher.presentation.screen.ManageVoucherScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class ManageVoucherExceptionHandler {
    suspend fun onManageVoucherError(
        syntax: Syntax<ManageVoucherScreenUiState, ManageVoucherScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce {
            state.copy(
                isLoading = false,
                isLoadingMore = false,
                error = throwable.message ?: "Unknown error occurred"
            )
        }

        Log.e(TAG, "onManageVoucherError: ${throwable.message}", throwable)
    }

    suspend fun onAddVoucherError(
        syntax: Syntax<AddVoucherScreenUiState, AddVoucherScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }
        postSideEffect(AddVoucherScreenUiEvent.OnCreateVoucherFailed(throwable.message.orEmpty()))
        Log.e(TAG, "onAddVoucherError: ${throwable.message}", throwable)
    }

    suspend fun onEditVoucherError(
        syntax: Syntax<EditVoucherScreenUiState, EditVoucherScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }
        postSideEffect(EditVoucherScreenUiEvent.OnUpdateVoucherFailed(throwable.message.orEmpty()))
        Log.e(TAG, "onEditVoucherError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "ManageVoucherExceptionHandler"
    }
}
