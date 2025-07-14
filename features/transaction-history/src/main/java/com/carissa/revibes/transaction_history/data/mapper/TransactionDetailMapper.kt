package com.carissa.revibes.transaction_history.data.mapper

import com.carissa.revibes.core.presentation.components.components.TransactionItem
import com.carissa.revibes.core.presentation.util.DateUtil
import com.carissa.revibes.transaction_history.data.model.TransactionDetailData
import com.carissa.revibes.transaction_history.domain.model.TransactionDetailDomain
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

fun TransactionDetailData.toTransactionDetailDomain(): TransactionDetailDomain {
    val items = this.items.map { item ->
        TransactionItem(
            id = item.id,
            name = item.name,
            type = item.type,
            weight = "${item.weight} kg",
            photos = item.media.map { it.downloadUri }
        )
    }.toImmutableList()

    val itemPoints = this.items.mapIndexed { index, item ->
        index.toString() to item.point
    }.toMap().toImmutableMap()
    val locationAddress = storeLocation.ifBlank { addressDetail?.ifBlank { address } ?: address }

    return TransactionDetailDomain(
        id = id,
        type = type,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
        country = country,
        address = address,
        addressDetail = addressDetail,
        postalCode = postalCode,
        storeLocation = storeLocation,
        status = status,
        totalPoint = totalPoint,
        items = items,
        itemPoints = itemPoints,
        locationAddress = locationAddress,
        formattedDate = DateUtil.formatDate(createdAt),
        formattedStatus = status.replaceFirstChar { it.uppercase() }
    )
}
