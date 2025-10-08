package com.carissa.revibes.manage_users.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.exchange_points.domain.model.UserVoucher
import com.carissa.revibes.manage_users.data.ManageUsersRepository
import com.carissa.revibes.manage_users.domain.model.UserDomain
import com.carissa.revibes.manage_users.presentation.handler.ManageUsersExceptionHandler
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.android.annotation.KoinViewModel

data class EditUserScreenUiState(
    val isLoading: Boolean = false,
    val isLoadingAddPoints: Boolean = false,
    val isLoadingDeductPoints: Boolean = false,
    val isLoadingEditUser: Boolean = false,
    val isLoadingVouchers: Boolean = false,
    val isRedeemingVoucher: Boolean = false,
    val user: UserDomain? = null,
    val userVouchers: ImmutableList<UserVoucher> = persistentListOf(),
    val pointsToAdd: TextFieldValue = TextFieldValue(),
    val pointsToDeduct: TextFieldValue = TextFieldValue(),
    val pointsError: String? = null,
    val deductPointsError: String? = null,
    val vouchersError: String? = null,
    val error: String? = null,
    val showAddPointsDialog: Boolean = false,
    val showDeductPointsDialog: Boolean = false,
    val showEditUserDialog: Boolean = false,
    val showRedeemConfirmDialog: Boolean = false,
    val voucherToRedeem: UserVoucher? = null,
    val isSuccess: Boolean = false,
    val editUserName: TextFieldValue = TextFieldValue(),
    val editUserEmail: TextFieldValue = TextFieldValue(),
    val editUserPhone: TextFieldValue = TextFieldValue(),
    val editUserRole: UserDomain.UserRole = UserDomain.UserRole.USER,
    val editUserNameError: String? = null,
    val editUserEmailError: String? = null,
    val editUserPhoneError: String? = null
)

sealed interface EditUserScreenUiEvent {
    data class SetUserId(val userId: String) : EditUserScreenUiEvent
    data object LoadUserDetail : EditUserScreenUiEvent
    data object LoadUserVouchers : EditUserScreenUiEvent
    data object ShowAddPointsDialog : EditUserScreenUiEvent
    data object HideAddPointsDialog : EditUserScreenUiEvent
    data object ShowDeductPointsDialog : EditUserScreenUiEvent
    data object HideDeductPointsDialog : EditUserScreenUiEvent
    data object ShowEditUserDialog : EditUserScreenUiEvent
    data object HideEditUserDialog : EditUserScreenUiEvent
    data class ShowRedeemConfirmDialog(val voucher: UserVoucher) : EditUserScreenUiEvent
    data object HideRedeemConfirmDialog : EditUserScreenUiEvent
    data object RedeemVoucher : EditUserScreenUiEvent
    data object AddPoints : EditUserScreenUiEvent
    data object DeductPoints : EditUserScreenUiEvent
    data object EditUser : EditUserScreenUiEvent
    data class PointsToAddChanged(val points: TextFieldValue) : EditUserScreenUiEvent
    data class PointsToDeductChanged(val points: TextFieldValue) : EditUserScreenUiEvent
    data class EditUserNameChanged(val name: TextFieldValue) : EditUserScreenUiEvent
    data class EditUserEmailChanged(val email: TextFieldValue) : EditUserScreenUiEvent
    data class EditUserPhoneChanged(val phone: TextFieldValue) : EditUserScreenUiEvent
    data class EditUserRoleChanged(val role: UserDomain.UserRole) : EditUserScreenUiEvent
    data class OnLoadUserFailed(val message: String) : EditUserScreenUiEvent
    data class OnAddPointsFailed(val message: String) : EditUserScreenUiEvent
    data class OnDeductPointsFailed(val message: String) : EditUserScreenUiEvent
    data class OnEditUserFailed(val message: String) : EditUserScreenUiEvent
    data class OnLoadVouchersFailed(val message: String) : EditUserScreenUiEvent
    data class OnRedeemVoucherFailed(val message: String) : EditUserScreenUiEvent
}

@KoinViewModel
class EditUserScreenViewModel(
    private val repository: ManageUsersRepository,
    private val exceptionHandler: ManageUsersExceptionHandler
) : BaseViewModel<EditUserScreenUiState, EditUserScreenUiEvent>(
    initialState = EditUserScreenUiState(),
    exceptionHandler = { syntax, throwable ->
        when {
            throwable.message?.contains("load", ignoreCase = true) == true ||
                throwable.message?.contains("fetch", ignoreCase = true) == true -> {
                exceptionHandler.onLoadUserError(syntax, throwable)
            }

            throwable.message?.contains("update", ignoreCase = true) == true ||
                throwable.message?.contains("edit", ignoreCase = true) == true -> syntax.run {
                postSideEffect(
                    EditUserScreenUiEvent.OnEditUserFailed(
                        throwable.message ?: "Failed to update user"
                    )
                )
            }

            else -> {
                exceptionHandler.onAddPointsError(syntax, throwable)
            }
        }
    }
) {

    private var userId: String = ""

    override fun onEvent(event: EditUserScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            is EditUserScreenUiEvent.SetUserId -> setUserId(event.userId)
            EditUserScreenUiEvent.LoadUserDetail -> loadUserDetail()
            EditUserScreenUiEvent.LoadUserVouchers -> loadUserVouchers()
            EditUserScreenUiEvent.ShowAddPointsDialog -> showAddPointsDialog()
            EditUserScreenUiEvent.HideAddPointsDialog -> hideAddPointsDialog()
            EditUserScreenUiEvent.ShowDeductPointsDialog -> showDeductPointsDialog()
            EditUserScreenUiEvent.HideDeductPointsDialog -> hideDeductPointsDialog()
            EditUserScreenUiEvent.ShowEditUserDialog -> showEditUserDialog()
            EditUserScreenUiEvent.HideEditUserDialog -> hideEditUserDialog()
            is EditUserScreenUiEvent.ShowRedeemConfirmDialog -> showRedeemConfirmDialog(event.voucher)
            EditUserScreenUiEvent.HideRedeemConfirmDialog -> hideRedeemConfirmDialog()
            EditUserScreenUiEvent.RedeemVoucher -> redeemVoucher()
            EditUserScreenUiEvent.AddPoints -> addPoints()
            EditUserScreenUiEvent.DeductPoints -> deductPoints()
            EditUserScreenUiEvent.EditUser -> editUser()
            is EditUserScreenUiEvent.PointsToAddChanged -> onPointsToAddChanged(event.points)
            is EditUserScreenUiEvent.PointsToDeductChanged -> onPointsToDeductChanged(event.points)
            is EditUserScreenUiEvent.EditUserNameChanged -> onEditUserNameChanged(event.name)
            is EditUserScreenUiEvent.EditUserEmailChanged -> onEditUserEmailChanged(event.email)
            is EditUserScreenUiEvent.EditUserPhoneChanged -> onEditUserPhoneChanged(event.phone)
            is EditUserScreenUiEvent.EditUserRoleChanged -> onEditUserRoleChanged(event.role)
            else -> Unit
        }
    }

    private fun setUserId(id: String) {
        userId = id
        onEvent(EditUserScreenUiEvent.LoadUserDetail)
        onEvent(EditUserScreenUiEvent.LoadUserVouchers)
    }

    private fun loadUserDetail() = intent {
        if (userId.isEmpty()) return@intent

        reduce { state.copy(isLoading = true, error = null) }

        val user = repository.getUserDetail(userId)

        reduce {
            state.copy(
                isLoading = false,
                user = user,
                error = null
            )
        }
    }

    private fun loadUserVouchers() = intent {
        if (userId.isEmpty()) return@intent

        reduce { state.copy(isLoadingVouchers = true, vouchersError = null) }

        try {
            val vouchers = repository.getUserVouchers(userId)
            reduce {
                state.copy(
                    isLoadingVouchers = false,
                    userVouchers = vouchers.toImmutableList(),
                    vouchersError = null
                )
            }
        } catch (e: Exception) {
            reduce {
                state.copy(
                    isLoadingVouchers = false,
                    vouchersError = e.message ?: "Failed to load vouchers"
                )
            }
        }
    }

    private fun showAddPointsDialog() = intent {
        reduce {
            state.copy(
                showAddPointsDialog = true,
                pointsToAdd = TextFieldValue(),
                pointsError = null
            )
        }
    }

    private fun hideAddPointsDialog() = intent {
        reduce {
            state.copy(
                showAddPointsDialog = false,
                pointsToAdd = TextFieldValue(),
                pointsError = null
            )
        }
    }

    private fun onPointsToAddChanged(points: TextFieldValue) = intent {
        reduce {
            state.copy(
                pointsToAdd = points,
                pointsError = when {
                    points.text.isBlank() -> "Points amount is required"
                    points.text.toIntOrNull() == null -> "Please enter a valid number"
                    points.text.toIntOrNull()!! <= 0 -> "Points must be greater than 0"
                    else -> null
                }
            )
        }
    }

    private fun addPoints() = intent {
        val pointsAmount = state.pointsToAdd.text.toIntOrNull()
        if (pointsAmount == null || pointsAmount <= 0) {
            reduce {
                state.copy(
                    pointsError = when {
                        state.pointsToAdd.text.isBlank() -> "Points amount is required"
                        pointsAmount == null -> "Please enter a valid number"
                        pointsAmount <= 0 -> "Points must be greater than 0"
                        else -> null
                    }
                )
            }
            return@intent
        }

        reduce { state.copy(isLoadingAddPoints = true) }

        val updatedUser = repository.addPointsToUser(
            id = userId,
            points = pointsAmount,
        )
        println("ketai: User after adding points: $updatedUser")

        reduce {
            state.copy(
                isLoadingAddPoints = false,
                user = updatedUser,
                showAddPointsDialog = false,
                pointsToAdd = TextFieldValue(),
                pointsError = null,
                isSuccess = true
            )
        }
    }

    private fun showDeductPointsDialog() = intent {
        reduce {
            state.copy(
                showDeductPointsDialog = true,
                pointsToDeduct = TextFieldValue(),
                deductPointsError = null
            )
        }
    }

    private fun hideDeductPointsDialog() = intent {
        reduce {
            state.copy(
                showDeductPointsDialog = false,
                pointsToDeduct = TextFieldValue(),
                deductPointsError = null
            )
        }
    }

    private fun onPointsToDeductChanged(points: TextFieldValue) = intent {
        reduce {
            state.copy(
                pointsToDeduct = points,
                deductPointsError = when {
                    points.text.isBlank() -> "Points amount is required"
                    points.text.toIntOrNull() == null -> "Please enter a valid number"
                    points.text.toIntOrNull()!! <= 0 -> "Points must be greater than 0"
                    points.text.toIntOrNull()!! > (
                        state.user?.points
                            ?: 0
                        ) -> "Cannot deduct more points than user has"

                    else -> null
                }
            )
        }
    }

    private fun deductPoints() = intent {
        val pointsAmount = state.pointsToDeduct.text.toIntOrNull()
        val currentPoints = state.user?.points ?: 0

        if (pointsAmount == null || pointsAmount <= 0 || pointsAmount > currentPoints) {
            reduce {
                state.copy(
                    deductPointsError = when {
                        state.pointsToDeduct.text.isBlank() -> "Points amount is required"
                        pointsAmount == null -> "Please enter a valid number"
                        pointsAmount <= 0 -> "Points must be greater than 0"
                        pointsAmount > currentPoints -> "Cannot deduct more points than user has"
                        else -> null
                    }
                )
            }
            return@intent
        }

        reduce { state.copy(isLoadingDeductPoints = true) }

        // Dummy API call - always returns success
        val updatedUser = repository.deductPointsFromUser(
            id = userId,
            points = pointsAmount,
        )

        reduce {
            state.copy(
                isLoadingDeductPoints = false,
                user = updatedUser,
                showDeductPointsDialog = false,
                pointsToDeduct = TextFieldValue(),
                deductPointsError = null,
                isSuccess = true
            )
        }
    }

    private fun showEditUserDialog() = intent {
        val user = state.user ?: return@intent
        reduce {
            state.copy(
                showEditUserDialog = true,
                editUserName = TextFieldValue(user.name),
                editUserEmail = TextFieldValue(user.email),
                editUserPhone = TextFieldValue(user.phone),
                editUserRole = user.role,
                editUserNameError = null,
                editUserEmailError = null,
                editUserPhoneError = null
            )
        }
    }

    private fun hideEditUserDialog() = intent {
        reduce {
            state.copy(
                showEditUserDialog = false,
                editUserName = TextFieldValue(),
                editUserEmail = TextFieldValue(),
                editUserPhone = TextFieldValue(),
                editUserRole = UserDomain.UserRole.USER,
                editUserNameError = null,
                editUserEmailError = null,
                editUserPhoneError = null
            )
        }
    }

    private fun onEditUserNameChanged(name: TextFieldValue) = intent {
        reduce {
            state.copy(
                editUserName = name,
                editUserNameError = if (name.text.isBlank()) "Name is required" else null
            )
        }
    }

    private fun onEditUserEmailChanged(email: TextFieldValue) = intent {
        reduce {
            state.copy(
                editUserEmail = email,
                editUserEmailError = when {
                    email.text.isBlank() -> "Email is required"
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text)
                        .matches() -> "Please enter a valid email"

                    else -> null
                }
            )
        }
    }

    private fun onEditUserPhoneChanged(phone: TextFieldValue) = intent {
        reduce {
            state.copy(
                editUserPhone = phone,
                editUserPhoneError = if (phone.text.isBlank()) "Phone number is required" else null
            )
        }
    }

    private fun onEditUserRoleChanged(role: UserDomain.UserRole) = intent {
        reduce {
            state.copy(editUserRole = role)
        }
    }

    private fun editUser() = intent {
        val hasErrors = listOf(
            state.editUserNameError,
            state.editUserEmailError,
            state.editUserPhoneError
        ).any { it != null } || state.editUserName.text.isBlank() ||
            state.editUserEmail.text.isBlank() || state.editUserPhone.text.isBlank()

        if (hasErrors) {
            reduce {
                state.copy(
                    editUserNameError = if (state.editUserName.text.isBlank()) {
                        "Name is required"
                    } else {
                        state.editUserNameError
                    },
                    editUserEmailError = when {
                        state.editUserEmail.text.isBlank() -> "Email is required"
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(
                            state.editUserEmail.text
                        ).matches() -> "Please enter a valid email"

                        else -> state.editUserEmailError
                    },
                    editUserPhoneError = if (state.editUserPhone.text.isBlank()) {
                        "Phone number is required"
                    } else {
                        state.editUserPhoneError
                    }
                )
            }
            return@intent
        }

        reduce { state.copy(isLoadingEditUser = true) }

        try {
            val updatedUser = repository.updateUser(
                id = userId,
                name = state.editUserName.text,
                email = state.editUserEmail.text,
                phone = state.editUserPhone.text,
                role = state.editUserRole
            )

            reduce {
                state.copy(
                    isLoadingEditUser = false,
                    user = updatedUser,
                    showEditUserDialog = false,
                    editUserName = TextFieldValue(),
                    editUserEmail = TextFieldValue(),
                    editUserPhone = TextFieldValue(),
                    editUserRole = UserDomain.UserRole.USER,
                    editUserNameError = null,
                    editUserEmailError = null,
                    editUserPhoneError = null,
                    isSuccess = true
                )
            }
        } catch (e: Exception) {
            reduce {
                state.copy(
                    isLoadingEditUser = false,
                    showEditUserDialog = false,
                    editUserName = TextFieldValue(),
                    editUserEmail = TextFieldValue(),
                    editUserPhone = TextFieldValue(),
                    editUserRole = UserDomain.UserRole.USER,
                    editUserNameError = null,
                    editUserEmailError = null,
                    editUserPhoneError = null
                )
            }
            postSideEffect(
                EditUserScreenUiEvent.OnEditUserFailed(
                    e.message ?: "Failed to update user"
                )
            )
        }
    }

    private fun showRedeemConfirmDialog(voucher: UserVoucher) = intent {
        reduce {
            state.copy(
                showRedeemConfirmDialog = true,
                voucherToRedeem = voucher
            )
        }
    }

    private fun hideRedeemConfirmDialog() = intent {
        reduce {
            state.copy(
                showRedeemConfirmDialog = false,
                voucherToRedeem = null
            )
        }
    }

    private fun redeemVoucher() = intent {
        val voucher = state.voucherToRedeem ?: return@intent

        reduce { state.copy(isRedeemingVoucher = true) }

        try {
            repository.redeemVoucher(userId, voucher.id)

            reduce {
                state.copy(
                    isRedeemingVoucher = false,
                    showRedeemConfirmDialog = false,
                    voucherToRedeem = null,
                    isSuccess = true
                )
            }

            onEvent(EditUserScreenUiEvent.LoadUserVouchers)
        } catch (e: Exception) {
            reduce {
                state.copy(
                    isRedeemingVoucher = false,
                    showRedeemConfirmDialog = false,
                    voucherToRedeem = null
                )
            }
            postSideEffect(
                EditUserScreenUiEvent.OnRedeemVoucherFailed(
                    e.message ?: "Failed to redeem voucher"
                )
            )
        }
    }
}
