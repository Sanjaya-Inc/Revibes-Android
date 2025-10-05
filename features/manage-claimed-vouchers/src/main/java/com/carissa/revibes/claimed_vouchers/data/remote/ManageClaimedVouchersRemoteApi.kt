package com.carissa.revibes.claimed_vouchers.data.remote

import com.carissa.revibes.claimed_vouchers.data.model.ClaimedVoucherResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Single

interface ManageClaimedVouchersRemoteApi {
    @GET("exchanges/transactions")
    suspend fun getClaimedVouchers(
        @Query("limit") limit: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortOrder") sortOrder: String = "desc",
        @Query("lastDocId") lastDocId: String? = null,
        @Query("direction") direction: String = "next"
    ): ClaimedVoucherResponse
}

@Single
internal class ManageClaimedVouchersRemoteApiImpl(ktorfit: Ktorfit) :
    ManageClaimedVouchersRemoteApi by ktorfit.createManageClaimedVouchersRemoteApi()
