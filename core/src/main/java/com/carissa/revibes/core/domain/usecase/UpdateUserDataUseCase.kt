package com.carissa.revibes.core.domain.usecase

import com.carissa.revibes.core.data.user.model.UserData

interface UpdateUserDataUseCase {
    suspend fun getAndUpdate(): UserData
}
