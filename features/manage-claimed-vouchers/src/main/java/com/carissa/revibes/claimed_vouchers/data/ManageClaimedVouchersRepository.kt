package com.carissa.revibes.claimed_vouchers.data

import com.carissa.revibes.claimed_vouchers.data.mapper.toDomain
import com.carissa.revibes.claimed_vouchers.data.remote.ManageClaimedVouchersRemoteApi
import com.carissa.revibes.claimed_vouchers.domain.model.ClaimedVoucherDomain
import com.carissa.revibes.core.data.utils.BaseRepository
import org.koin.core.annotation.Single

@Single
internal class ManageClaimedVouchersRepository(
    private val remoteApi: ManageClaimedVouchersRemoteApi
) : BaseRepository() {

    suspend fun getClaimedVouchers(
        limit: Int = 10,
        sortBy: String = "createdAt",
        sortOrder: String = "desc",
        lastDocId: String? = null,
        direction: String = "next"
    ): List<ClaimedVoucherDomain> {
        return execute {
            remoteApi.getClaimedVouchers(
                limit = limit,
                sortBy = sortBy,
                sortOrder = sortOrder,
                lastDocId = lastDocId,
                direction = direction
            ).data.items.map { it.toDomain() }
        }
    }
}
