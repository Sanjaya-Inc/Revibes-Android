package com.carissa.revibes.manage_voucher.data

import android.content.Context
import android.net.Uri
import com.carissa.revibes.core.data.utils.BaseRepository
import com.carissa.revibes.manage_voucher.data.mapper.toDomain
import com.carissa.revibes.manage_voucher.data.model.PaginationData
import com.carissa.revibes.manage_voucher.data.model.UpdateVoucherStatusRequest
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

@Single
class ManageVoucherRepository(
    private val remoteApi: ManageVoucherRemoteApi
) : BaseRepository() {

    suspend fun getVoucherList(
        limit: Int = 10,
        sortBy: String = "createdAt",
        sortOrder: String = "desc",
        lastDocId: String? = null,
        direction: String = "next"
    ): VoucherListResult {
        return execute {
            val response = remoteApi.getVoucherList(
                limit = limit,
                sortBy = sortBy,
                sortOrder = sortOrder,
                lastDocId = lastDocId,
                direction = direction
            )

            val vouchers = response.data.items.map { it.toDomain() }
            val pagination = response.data.pagination

            VoucherListResult(
                vouchers = vouchers,
                pagination = pagination
            )
        }
    }

    suspend fun getVoucherDetail(id: String): VoucherDomain {
        return execute {
            val response = remoteApi.getVoucherDetail(id)
            response.data.toDomain()
        }
    }

    @OptIn(InternalAPI::class)
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
        imageUri: Uri,
        termConditions: List<String> = emptyList(),
        guides: List<String> = emptyList()
    ) {
        execute {
            val conditionsJson = Json.encodeToString(
                serializer = VoucherConditions.serializer(),
                value = conditions
            )

            val termConditionsJson = Json.encodeToString(termConditions)
            val guidesJson = Json.encodeToString(guides)

            val inputStream = context.contentResolver.openInputStream(imageUri)
            val imageBytes =
                inputStream?.readBytes() ?: throw IllegalArgumentException("Cannot read image")
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
                    append("\"termConditions\"", termConditionsJson)
                    append("\"guides\"", guidesJson)
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
    }

    suspend fun deleteVoucher(id: String) {
        execute { remoteApi.deleteVoucher(id) }
    }

    suspend fun updateVoucherStatus(id: String, isAvailable: Boolean) {
        val request = UpdateVoucherStatusRequest(isAvailable = isAvailable)
        remoteApi.updateVoucherStatus(id, request)
    }

    suspend fun updateVoucher(
        id: String,
        name: String,
        description: String,
        code: String? = null,
        conditions: VoucherConditions? = null,
        claimPeriodStart: String? = null,
        claimPeriodEnd: String? = null,
        termConditions: List<String>? = null,
        guides: List<String>? = null
    ) {
        execute {
            val conditionsRequest = conditions?.let {
                com.carissa.revibes.manage_voucher.data.model.VoucherConditionsRequest(
                    maxClaim = it.maxClaim,
                    maxUsage = it.maxUsage,
                    minOrderItem = it.minOrderItem,
                    minOrderAmount = it.minOrderAmount.toDouble(),
                    maxDiscountAmount = it.maxDiscountAmount.toDouble()
                )
            }

            val request = com.carissa.revibes.manage_voucher.data.model.UpdateVoucherRequest(
                name = name,
                description = description,
                code = code,
                conditions = conditionsRequest,
                claimPeriodStart = claimPeriodStart,
                claimPeriodEnd = claimPeriodEnd,
                termConditions = termConditions,
                guides = guides
            )

            remoteApi.updateVoucher(id, request)
        }
    }
}
