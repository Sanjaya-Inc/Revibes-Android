package com.carissa.revibes.core.presentation.model

import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.domain.usecase.UpdateUserDataUseCase
import com.carissa.revibes.core.presentation.util.AppScope
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
@Single
class UserPointFlow(
    @Provided private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val userDataSource: UserDataSource,
    private val appScope: AppScope,
    private val upstream: MutableStateFlow<Int> = MutableStateFlow(
        userDataSource.getUserValue().getOrNull()?.coins ?: 0
    )
) : StateFlow<Int> by upstream {

    fun update() = appScope.launch {
        val userData = updateUserDataUseCase.getAndUpdate()
        upstream.update { userData.coins }
    }

    fun update(point: Int) {
        upstream.update { point }
    }
}
