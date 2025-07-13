/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.auth.data.local

import com.carissa.revibes.core.data.main.local.LocalDataSource

fun interface DeviceIdDataSourceGetter {
    fun getDeviceIdValue(): String?
}

internal class DeviceIdDataSourceGetterImpl(
    private val localDataSource: LocalDataSource
) : DeviceIdDataSourceGetter {
    override fun getDeviceIdValue(): String? {
        return localDataSource.getString(DeviceIdDataSource.KEY, null)
    }
}
