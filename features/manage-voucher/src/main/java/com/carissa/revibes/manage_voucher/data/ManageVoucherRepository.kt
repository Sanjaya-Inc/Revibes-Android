package com.carissa.revibes.manage_voucher.data

import android.content.Context
import android.net.Uri
import com.carissa.revibes.manage_voucher.data.mapper.toDomain
import com.carissa.revibes.manage_voucher.data.model.PaginationData
import com.carissa.revibes.manage_voucher.data.remote.ManageVoucherRemoteApi
import com.carissa.revibes.manage_voucher.domain.model.VoucherConditions
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.InternalAPI
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

data class VoucherListResult(
    val vouchers: List<VoucherDomain>,
    val pagination: PaginationData
)

interface ManageVoucherRepository {
    suspend fun getVoucherList(
        limit: Int = 10,
        sortBy: String = "createdAt",
        sortOrder: String = "desc",
        lastDocId: String? = null,
        direction: String = "next"
    ): VoucherListResult

    suspend fun getVoucherDetail(id: String): VoucherDomain

    suspend fun createVoucher(
        context: Context,
        code: String,
        name: String,
        description: String,
        type: VoucherDomain.VoucherType,
        amount: Double,
        conditions: VoucherConditions,
        claimPeriodStart: String,
        claimPeriodEnd: String,
        imageUri: Uri
    )

    suspend fun deleteVoucher(id: String)
}

@Single
internal class ManageVoucherRepositoryImpl(
    private val remoteApi: ManageVoucherRemoteApi
) : ManageVoucherRepository {

    override suspend fun getVoucherList(
        limit: Int,
        sortBy: String,
        sortOrder: String,
        lastDocId: String?,
        direction: String
    ): VoucherListResult {
        val response = remoteApi.getVoucherList(
            limit = limit,
            sortBy = sortBy,
            sortOrder = sortOrder,
            lastDocId = lastDocId,
            direction = direction
        )

        val vouchers = response.data.items.map { it.toDomain() }
        val pagination = response.data.pagination

        return VoucherListResult(
            vouchers = vouchers,
            pagination = pagination
        )
    }

    override suspend fun getVoucherDetail(id: String): VoucherDomain {
        val response = remoteApi.getVoucherDetail(id)
        return response.data.toDomain()
    }

    @OptIn(InternalAPI::class)
    override suspend fun createVoucher(
        context: Context,
        code: String,
        name: String,
        description: String,
        type: VoucherDomain.VoucherType,
        amount: Double,
        conditions: VoucherConditions,
        claimPeriodStart: String,
        claimPeriodEnd: String,
        imageUri: Uri
    ) {
        val conditionsJson = Json.encodeToString(
            serializer = VoucherConditions.serializer(),
            value = conditions
        )

        val inputStream = context.contentResolver.openInputStream(imageUri)
        val imageBytes = inputStream?.readBytes() ?: throw IllegalArgumentException("Cannot read image")
        inputStream.close()
        val multipart = MultiPartFormDataContent(
            formData {
                append("\"code\"", code)
                append("\"name\"", name)
                append("\"description\"", description)
                append("\"type\"", type.toString())
                append("\"amount\"", amount)
                append("\"conditions\"", conditionsJson)
                append("\"claimPeriodStart\"", claimPeriodStart)
                append("\"claimPeriodEnd\"", claimPeriodEnd)
                append(
                    "\"image\"",
                    imageBytes,
                    Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
                        append(HttpHeaders.ContentDisposition, "filename=\"voucher_image.jpg\"")
                    }
                )
            }
        )

        remoteApi.createVoucher(
            data = multipart
        )
    }

    override suspend fun deleteVoucher(id: String) {
        remoteApi.deleteVoucher(id)
    }
}
