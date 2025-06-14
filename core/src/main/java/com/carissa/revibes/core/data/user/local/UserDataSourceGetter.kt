/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.core.data.user.local

import android.util.Log
import com.carissa.revibes.core.data.main.local.LocalDataSource
import com.carissa.revibes.core.data.user.model.UserData
import kotlinx.serialization.json.Json

fun interface UserDataSourceGetter {
    fun getUserValue(): Result<UserData>
}

internal class UserDataSourceGetterImpl(
    private val localDataSource: LocalDataSource,
    private val json: Json
) : UserDataSourceGetter {
    override fun getUserValue(): Result<UserData> {
        return localDataSource.runCatching<LocalDataSource, UserData> {
            val userDataRaw = requireNotNull(getString(UserDataSource.KEY, null))
            json.decodeFromString(userDataRaw)
        }.onFailure {
            Log.e(TAG, "getUserValue: $it")
        }
    }

    companion object {
        private const val TAG = "UserDataSourceGetter"
    }
}
