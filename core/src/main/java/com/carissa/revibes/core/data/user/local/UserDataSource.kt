/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.core.data.user.local

import com.carissa.revibes.core.data.main.local.LocalDataSource
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

/**
 * Return type UserData with default value UserData.dummy()
 **/
interface UserDataSource :
    UserDataSourceSetter,
    UserDataSourceGetter {
    companion object {
        const val KEY = "user_data"
    }
}

@Single
internal class UserDataSourceImpl(
    private val localDataSource: LocalDataSource,
    private val json: Json
) : UserDataSource,
    UserDataSourceSetter by UserDataSourceSetterImpl(localDataSource, json),
    UserDataSourceGetter by UserDataSourceGetterImpl(localDataSource, json)
