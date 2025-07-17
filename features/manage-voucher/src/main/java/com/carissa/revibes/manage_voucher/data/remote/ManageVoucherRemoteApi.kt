package com.carissa.revibes.manage_voucher.data.remote

import com.carissa.revibes.manage_voucher.data.model.CreateVoucherRequest
import com.carissa.revibes.manage_voucher.data.model.UpdateVoucherRequest
import com.carissa.revibes.manage_voucher.data.model.VoucherDetailResponse
import com.carissa.revibes.manage_voucher.data.model.VoucherListResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import org.koin.core.annotation.Single

interface ManageVoucherRemoteApi {

    @GET("admin/vouchers")
    suspend fun getVoucherList(
        @Query("limit") limit: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortOrder") sortOrder: String = "desc",
        @Query("lastDocId") lastDocId: String? = null,
        @Query("direction") direction: String = "next"
    ): VoucherListResponse

    @GET("admin/vouchers/{id}")
    suspend fun getVoucherDetail(
        @Path("id") id: String
    ): VoucherDetailResponse

    @POST("admin/vouchers")
    suspend fun createVoucher(
        @Body request: CreateVoucherRequest
    ): VoucherDetailResponse

    @PUT("admin/vouchers/{id}")
    suspend fun updateVoucher(
        @Path("id") id: String,
        @Body request: UpdateVoucherRequest
    ): VoucherDetailResponse

    @DELETE("admin/vouchers/{id}")
    suspend fun deleteVoucher(
        @Path("id") id: String
    ): VoucherDetailResponse
}

@Single
internal class ManageVoucherRemoteApiImpl(ktorfit: Ktorfit) :
    ManageVoucherRemoteApi by ktorfit.createManageVoucherRemoteApi()
