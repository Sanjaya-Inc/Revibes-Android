package com.carissa.revibes.manage_voucher.data.remote

import com.carissa.revibes.manage_voucher.data.model.BaseResponse
import com.carissa.revibes.manage_voucher.data.model.UpdateVoucherRequest
import com.carissa.revibes.manage_voucher.data.model.UpdateVoucherStatusRequest
import com.carissa.revibes.manage_voucher.data.model.VoucherDetailResponse
import com.carissa.revibes.manage_voucher.data.model.VoucherListResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.request.forms.MultiPartFormDataContent
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
    suspend fun createVoucher(
        @Body data: MultiPartFormDataContent,
    ): BaseResponse

    @DELETE("vouchers/{id}")
    suspend fun deleteVoucher(
        @Path("id") id: String
    ): BaseResponse

    @PATCH("vouchers/{id}/status")
    @Headers("Content-Type: application/json")
    suspend fun updateVoucherStatus(
        @Path("id") id: String,
        @Body request: UpdateVoucherStatusRequest
    ): BaseResponse

    @PUT("vouchers/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateVoucher(
        @Path("id") id: String,
        @Body request: UpdateVoucherRequest
    ): BaseResponse
}

@Single
internal class ManageVoucherRemoteApiImpl(ktorfit: Ktorfit) :
    ManageVoucherRemoteApi by ktorfit.createManageVoucherRemoteApi()
