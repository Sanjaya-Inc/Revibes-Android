/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.core.data.user.local

import android.util.Log
import androidx.core.content.edit
import com.carissa.revibes.core.data.main.local.LocalDataSource
import com.carissa.revibes.core.data.user.model.UserData
import kotlinx.serialization.json.Json

interface UserDataSourceSetter {
    fun setUserValue(value: UserData): Result<UserData>
    fun clearUserData()
}

internal class UserDataSourceSetterImpl(
    private val localDataSource: LocalDataSource,
    private val json: Json
) : UserDataSourceSetter {
    override fun setUserValue(value: UserData): Result<UserData> {
        return localDataSource.runCatching {
            val userDataRaw = json.encodeToString(value)
            localDataSource.edit {
                putString(UserDataSource.KEY, userDataRaw)
            }
            value
        }.onFailure {
            Log.e(TAG, "setUserValue: $it")
        }
    }

    companion object {
        private const val TAG = "UserDataSourceSetter"
    }

    override fun clearUserData() {
        localDataSource.edit {
            remove(UserDataSource.KEY)
        }
    }
}
