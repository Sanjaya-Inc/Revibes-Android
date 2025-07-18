package com.carissa.revibes.manage_transaction.data.mapper

import com.carissa.revibes.manage_transaction.domain.model.ManageTransactionDomain
import com.carissa.revibes.manage_transaction.domain.model.TransactionDetailDomain
import com.carissa.revibes.manage_transaction.domain.model.TransactionDetailItemDomain
import com.carissa.revibes.manage_transaction.domain.model.TransactionDetailMediaDomain
import com.carissa.revibes.manage_transaction.domain.model.TransactionItemDomain
import com.carissa.revibes.manage_transaction.domain.model.TransactionMediaDomain
import com.carissa.revibes.manage_transaction.domain.model.TransactionStatus
import com.carissa.revibes.transaction_history.data.model.LogisticItemData
import com.carissa.revibes.transaction_history.data.model.LogisticOrderData
import com.carissa.revibes.transaction_history.data.model.MediaData
import com.carissa.revibes.transaction_history.data.model.TransactionDetailData
import com.carissa.revibes.transaction_history.data.model.TransactionDetailItemData
import com.carissa.revibes.transaction_history.data.model.TransactionDetailMediaData

fun LogisticOrderData.toDomain(): ManageTransactionDomain {
    return ManageTransactionDomain(
        id = id,
        type = type,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
        country = country,
        address = address,
        postalCode = postalCode,
        storeLocation = storeLocation,
        status = TransactionStatus.fromString(status),
        maker = maker,
        items = items.map { it.toDomain() },
        totalPoint = totalPoint
    )
}

fun LogisticItemData.toDomain(): TransactionItemDomain {
    return TransactionItemDomain(
        id = id,
        name = name,
        type = type,
        weight = weight,
        point = point,
        media = media.map { it.toDomain() }
    )
}

fun MediaData.toDomain(): TransactionMediaDomain {
    return TransactionMediaDomain(
        uploadUrl = uploadUrl,
        downloadUri = downloadUri,
        expiredAt = expiredAt
    )
}

fun TransactionDetailData.toDomain(): TransactionDetailDomain {
    return TransactionDetailDomain(
        id = id,
        type = type,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name,
        country = country,
        address = store.address,
        postalCode = store.postalCode,
        storeName = store.name,
        items = items.map { it.toDomain() },
        status = TransactionStatus.fromString(status),
        totalPoint = totalPoint
    )
}

fun TransactionDetailItemData.toDomain(): TransactionDetailItemDomain {
    return TransactionDetailItemDomain(
        id = id,
        name = name,
        type = type,
        weight = weight,
        point = point,
        media = media.map { it.toDomain() }
    )
}

fun TransactionDetailMediaData.toDomain(): TransactionDetailMediaDomain {
    return TransactionDetailMediaDomain(
        uploadUrl = uploadUrl,
        downloadUri = downloadUri,
    )
}
