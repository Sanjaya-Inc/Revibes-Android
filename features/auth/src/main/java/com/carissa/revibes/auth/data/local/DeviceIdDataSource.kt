/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.auth.data.local

import com.carissa.revibes.core.data.main.local.LocalDataSource
import org.koin.core.annotation.Single
import java.util.UUID

interface DeviceIdDataSource :
    DeviceIdDataSourceSetter,
    DeviceIdDataSourceGetter {
    companion object {
        const val KEY = "device_id"
    }

    fun getOrGenerateDeviceId(): String
}

@Single
internal class DeviceIdDataSourceImpl(
    private val localDataSource: LocalDataSource
) : DeviceIdDataSource,
    DeviceIdDataSourceSetter by DeviceIdDataSourceSetterImpl(localDataSource),
    DeviceIdDataSourceGetter by DeviceIdDataSourceGetterImpl(localDataSource) {

    override fun getOrGenerateDeviceId(): String {
        val existingId = getDeviceIdValue()
        return if (existingId != null) {
            existingId
        } else {
            val newId = UUID.randomUUID().toString()
            setDeviceIdValue(newId)
            newId
        }
    }
}
