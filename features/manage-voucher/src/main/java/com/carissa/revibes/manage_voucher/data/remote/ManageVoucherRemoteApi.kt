package com.carissa.revibes.manage_voucher.data.remote

import com.carissa.revibes.manage_voucher.data.model.BaseResponse
import com.carissa.revibes.manage_voucher.data.model.VoucherDetailResponse
import com.carissa.revibes.manage_voucher.data.model.VoucherListResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Part
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.http.content.PartData
import org.koin.core.annotation.Single

interface ManageVoucherRemoteApi {

    @GET("vouchers")
    suspend fun getVoucherList(
        @Query("limit") limit: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortOrder") sortOrder: String = "desc",
        @Query("lastDocId") lastDocId: String? = null,
        @Query("direction") direction: String = "next"
    ): VoucherListResponse

    @GET("vouchers/{id}")
    suspend fun getVoucherDetail(
        @Path("id") id: String
    ): VoucherDetailResponse

    @POST("vouchers")
    @Multipart
    suspend fun createVoucher(
        @Part("code") code: String,
        @Part("name") name: String,
        @Part("description") description: String,
        @Part("type") type: String,
        @Part("amount") amount: String,
        @Part("conditions") conditions: String,
        @Part("claimPeriodStart") claimPeriodStart: String,
        @Part("claimPeriodEnd") claimPeriodEnd: String,
        @Part("") image: List<PartData>
    ): BaseResponse

    @DELETE("vouchers/{id}")
    suspend fun deleteVoucher(
        @Path("id") id: String
    ): BaseResponse
}

@Single
internal class ManageVoucherRemoteApiImpl(ktorfit: Ktorfit) :
    ManageVoucherRemoteApi by ktorfit.createManageVoucherRemoteApi()
