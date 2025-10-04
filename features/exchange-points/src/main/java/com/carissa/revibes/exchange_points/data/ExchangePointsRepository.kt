package com.carissa.revibes.exchange_points.data

import com.carissa.revibes.core.data.utils.BaseRepository
import com.carissa.revibes.exchange_points.data.mapper.toVoucher
import com.carissa.revibes.exchange_points.data.model.PurchaseRequest
import com.carissa.revibes.exchange_points.data.remote.ExchangePointsRemoteApi
import com.carissa.revibes.exchange_points.domain.model.Voucher
import org.koin.core.annotation.Single

@Single
class ExchangePointsRepository(
    private val remoteApi: ExchangePointsRemoteApi
) : BaseRepository() {

    suspend fun getVouchers(): List<Voucher> {
        return execute { remoteApi.getVouchers().data.items.map { it.toVoucher() } }
    }

    suspend fun purchaseVoucher(purchaseRequest: PurchaseRequest) {
        execute { remoteApi.purchaseVoucher(purchaseRequest) }
    }
}
