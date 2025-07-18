package com.carissa.revibes.manage_voucher.data

import com.carissa.revibes.manage_voucher.data.mapper.toDomain
import com.carissa.revibes.manage_voucher.data.model.PaginationData
import com.carissa.revibes.manage_voucher.data.remote.ManageVoucherRemoteApi
import com.carissa.revibes.manage_voucher.domain.model.VoucherConditions
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
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
        code: String,
        name: String,
        description: String,
        type: VoucherDomain.VoucherType,
        amount: Double,
//        currency: VoucherDomain.Currency,
        conditions: VoucherConditions,
        claimPeriodStart: String,
        claimPeriodEnd: String,
        imageUrl: String? = null
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

    override suspend fun createVoucher(
        code: String,
        name: String,
        description: String,
        type: VoucherDomain.VoucherType,
        amount: Double,
//        currency: VoucherDomain.Currency,
        conditions: VoucherConditions,
        claimPeriodStart: String,
        claimPeriodEnd: String,
        imageUrl: String?
    ) {
        val conditions = Json.encodeToString(
            serializer = VoucherConditions.serializer(),
            value = conditions
        )
        remoteApi.createVoucher(
            code = code,
            name = name,
            description = description,
            type = type.toString(),
            amount = amount.toString(),
//            currency = currency.toString(),
            conditions = conditions,
            claimPeriodStart = claimPeriodStart,
            claimPeriodEnd = claimPeriodEnd,
//            image = null
        )
    }

    override suspend fun deleteVoucher(id: String) {
        remoteApi.deleteVoucher(id)
    }
}
