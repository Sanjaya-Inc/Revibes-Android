package com.carissa.revibes.drop_off.data.mapper

import com.carissa.revibes.drop_off.data.model.Store
import com.carissa.revibes.drop_off.domain.model.StoreData

internal fun Store.toStoreData(): StoreData {
    return StoreData(
        id = id,
        name = name,
        country = country,
        address = address,
        postalCode = postalCode,
        latitude = position.latitude,
        longitude = position.longitude,
        distance = position.distance,
        status = status
    )
}

internal fun List<Store>.toStoreDataList(): List<StoreData> {
    return map { it.toStoreData() }
}
