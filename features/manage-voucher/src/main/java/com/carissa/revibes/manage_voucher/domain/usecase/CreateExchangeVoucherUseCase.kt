package com.carissa.revibes.manage_voucher.domain.usecase

import com.carissa.revibes.core.domain.utils.BaseUseCase
import com.carissa.revibes.manage_voucher.data.ManageVoucherRepository
import com.carissa.revibes.manage_voucher.domain.model.ExchangeVoucherDomain
import org.koin.core.annotation.Factory

@Factory
class CreateExchangeVoucherUseCase(
    private val repository: ManageVoucherRepository
) : BaseUseCase() {

    suspend operator fun invoke(
        sourceId: String,
        amount: Int,
        description: String = "",
        quota: Int = -1,
        endedAt: String? = null
    ): ExchangeVoucherDomain {
        return execute {
            repository.createExchangeVoucher(
                sourceId = sourceId,
                amount = amount,
                description = description,
                quota = quota,
                endedAt = endedAt
            )
        }
    }
}
