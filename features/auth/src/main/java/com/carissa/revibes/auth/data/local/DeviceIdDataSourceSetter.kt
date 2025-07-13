/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.auth.data.local

import androidx.core.content.edit
import com.carissa.revibes.core.data.main.local.LocalDataSource

fun interface DeviceIdDataSourceSetter {
    fun setDeviceIdValue(value: String)
}

internal class DeviceIdDataSourceSetterImpl(
    private val localDataSource: LocalDataSource
) : DeviceIdDataSourceSetter {
    override fun setDeviceIdValue(value: String) {
        localDataSource.edit {
            putString(DeviceIdDataSource.KEY, value)
        }
    }
}
