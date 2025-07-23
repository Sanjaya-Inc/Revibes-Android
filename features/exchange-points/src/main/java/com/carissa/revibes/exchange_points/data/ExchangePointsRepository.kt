package com.carissa.revibes.exchange_points.data

import com.carissa.revibes.exchange_points.data.mapper.toVoucher
import com.carissa.revibes.exchange_points.data.remote.ExchangePointsRemoteApi
import com.carissa.revibes.exchange_points.domain.model.Voucher
import org.koin.core.annotation.Single

interface ExchangePointsRepository {
    suspend fun getVouchers(): List<Voucher>
}

@Single
internal class ExchangePointsRepositoryImpl(
    private val remoteApi: ExchangePointsRemoteApi
) : ExchangePointsRepository {

    override suspend fun getVouchers(): List<Voucher> {
        return remoteApi.getVouchers().data.items.map { it.toVoucher() }
    }
}
