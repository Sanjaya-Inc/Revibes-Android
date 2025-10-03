package com.carissa.revibes.transaction_history.data.mapper

import com.carissa.revibes.core.presentation.components.components.TransactionItem
import com.carissa.revibes.transaction_history.data.model.PositionData
import com.carissa.revibes.transaction_history.data.model.StoreData
import com.carissa.revibes.transaction_history.data.model.TransactionDetailData
import com.carissa.revibes.transaction_history.data.model.TransactionDetailItemData
import com.carissa.revibes.transaction_history.data.model.TransactionDetailMediaData
import com.carissa.revibes.transaction_history.domain.model.Position
import com.carissa.revibes.transaction_history.domain.model.Store
import com.carissa.revibes.transaction_history.domain.model.TransactionDetail
import com.carissa.revibes.transaction_history.domain.model.TransactionDetailItem
import com.carissa.revibes.transaction_history.domain.model.TransactionDetailMedia
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

fun TransactionDetailData.toDomain(): TransactionDetail {
    return TransactionDetail(
        id = id,
        type = type,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
        country = country,
        items = items.map { it.toDomain() },
        status = status,
        totalPoint = totalPoint,
        store = store?.toDomain() ?: Store(
            id = "",
            createdAt = "",
            updatedAt = "",
            name = "",
            country = "",
            address = "",
            postalCode = "",
            position = Position(latitude = 0.0, longitude = 0.0),
            status = ""
        )
    )
}

fun TransactionDetailItemData.toDomain(): TransactionDetailItem {
    return TransactionDetailItem(
        id = id,
        name = name,
        type = type,
        weight = weight,
        point = point,
        media = media.map { it.toDomain() }
    )
}

fun TransactionDetailMediaData.toDomain(): TransactionDetailMedia {
    return TransactionDetailMedia(
        downloadUri = downloadUri
    )
}

fun StoreData.toDomain(): Store {
    return Store(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
        country = country,
        address = address,
        postalCode = postalCode,
        position = position.toDomain(),
        status = status
    )
}

fun PositionData.toDomain(): Position {
    return Position(
        latitude = latitude,
        longitude = longitude
    )
}

fun TransactionDetailItem.toTransactionItem(): TransactionItem {
    return TransactionItem(
        id = this.id,
        name = this.name,
        type = this.type,
        weight = this.weight.toString(),
        photos = this.media.map { it.downloadUri }
    )
}

fun List<TransactionDetailItem>.toItemPoints(): ImmutableMap<String, Int> {
    return this.associate { it.id to it.point }.toImmutableMap()
}
