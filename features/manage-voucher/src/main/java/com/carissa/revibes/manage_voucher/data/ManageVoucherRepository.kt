package com.carissa.revibes.manage_voucher.data

import com.carissa.revibes.manage_voucher.data.model.PaginationData
import com.carissa.revibes.manage_voucher.data.remote.ManageVoucherRemoteApi
import com.carissa.revibes.manage_voucher.domain.model.VoucherConditions
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import kotlinx.coroutines.delay
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
        currency: VoucherDomain.Currency,
        conditions: VoucherConditions,
        claimPeriodStart: String,
        claimPeriodEnd: String,
        imageUrl: String? = null
    ): VoucherDomain

    suspend fun updateVoucher(
        id: String,
        code: String,
        name: String,
        description: String,
        type: VoucherDomain.VoucherType,
        amount: Double,
        currency: VoucherDomain.Currency,
        conditions: VoucherConditions,
        claimPeriodStart: String,
        claimPeriodEnd: String,
        imageUrl: String? = null,
        isActive: Boolean
    ): VoucherDomain

    suspend fun deleteVoucher(id: String): VoucherDomain
}

@Single
internal class ManageVoucherRepositoryImpl(
    private val remoteApi: ManageVoucherRemoteApi
) : ManageVoucherRepository {

    private val dummyVouchers = VoucherDomain.dummy().toMutableList()

    override suspend fun getVoucherList(
        limit: Int,
        sortBy: String,
        sortOrder: String,
        lastDocId: String?,
        direction: String
    ): VoucherListResult {
        delay(1000)

        val sortedVouchers = when (sortBy) {
            "createdAt" -> if (sortOrder == "desc") {
                dummyVouchers.sortedByDescending { it.createdAt }
            } else {
                dummyVouchers.sortedBy { it.createdAt }
            }
            "name" -> if (sortOrder == "desc") {
                dummyVouchers.sortedByDescending { it.name }
            } else {
                dummyVouchers.sortedBy { it.name }
            }
            else -> dummyVouchers
        }

        val startIndex = lastDocId?.let { id ->
            sortedVouchers.indexOfFirst { it.id == id }.takeIf { it >= 0 }?.plus(1) ?: 0
        } ?: 0

        val endIndex = minOf(startIndex + limit, sortedVouchers.size)
        val paginatedVouchers = sortedVouchers.subList(startIndex, endIndex)

        val pagination = PaginationData(
            currentPage = (startIndex / limit) + 1,
            totalPages = (sortedVouchers.size + limit - 1) / limit,
            totalItems = sortedVouchers.size,
            hasMoreNext = endIndex < sortedVouchers.size,
            hasMorePrev = startIndex > 0,
            lastDocId = paginatedVouchers.lastOrNull()?.id
        )

        return VoucherListResult(
            vouchers = paginatedVouchers,
            pagination = pagination
        )
    }

    override suspend fun getVoucherDetail(id: String): VoucherDomain {
        delay(500)
        return dummyVouchers.find { it.id == id }
            ?: throw Exception("Voucher not found")
    }

    override suspend fun createVoucher(
        code: String,
        name: String,
        description: String,
        type: VoucherDomain.VoucherType,
        amount: Double,
        currency: VoucherDomain.Currency,
        conditions: VoucherConditions,
        claimPeriodStart: String,
        claimPeriodEnd: String,
        imageUrl: String?
    ): VoucherDomain {
        delay(1000)

        val newVoucher = VoucherDomain(
            id = (dummyVouchers.size + 1).toString(),
            code = code,
            name = name,
            description = description,
            type = type,
            amount = amount,
            currency = currency,
            conditions = conditions,
            claimPeriodStart = claimPeriodStart,
            claimPeriodEnd = claimPeriodEnd,
            imageUrl = imageUrl,
            isActive = true,
            createdAt = "2024-01-01T00:00:00.000Z",
            updatedAt = "2024-01-01T00:00:00.000Z"
        )

        dummyVouchers.add(newVoucher)
        return newVoucher
    }

    override suspend fun updateVoucher(
        id: String,
        code: String,
        name: String,
        description: String,
        type: VoucherDomain.VoucherType,
        amount: Double,
        currency: VoucherDomain.Currency,
        conditions: VoucherConditions,
        claimPeriodStart: String,
        claimPeriodEnd: String,
        imageUrl: String?,
        isActive: Boolean
    ): VoucherDomain {
        delay(1000)

        val index = dummyVouchers.indexOfFirst { it.id == id }
        if (index == -1) throw Exception("Voucher not found")

        val updatedVoucher = dummyVouchers[index].copy(
            code = code,
            name = name,
            description = description,
            type = type,
            amount = amount,
            currency = currency,
            conditions = conditions,
            claimPeriodStart = claimPeriodStart,
            claimPeriodEnd = claimPeriodEnd,
            imageUrl = imageUrl,
            isActive = isActive,
            updatedAt = "2024-01-01T00:00:00.000Z"
        )

        dummyVouchers[index] = updatedVoucher
        return updatedVoucher
    }

    override suspend fun deleteVoucher(id: String): VoucherDomain {
        delay(500)

        val voucher = dummyVouchers.find { it.id == id }
            ?: throw Exception("Voucher not found")

        dummyVouchers.removeIf { it.id == id }
        return voucher
    }
}
