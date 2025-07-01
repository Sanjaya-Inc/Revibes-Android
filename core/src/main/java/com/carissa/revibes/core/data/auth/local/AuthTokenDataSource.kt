package com.carissa.revibes.core.data.auth.local

import androidx.core.content.edit
import com.carissa.revibes.core.data.main.local.LocalDataSource
import com.carissa.revibes.core.data.main.util.DataConstant
import org.koin.core.annotation.Single

interface AuthTokenDataSource {
    fun setAuthToken(token: String)
    fun getAuthToken(): String?
    fun clearAuthToken()
}

@Single
internal class AuthTokenDataSourceImpl(
    private val localDataSource: LocalDataSource
) : AuthTokenDataSource {

    override fun setAuthToken(token: String) {
        localDataSource.edit { putString(DataConstant.AUTH_TOKEN_PREF_KEY, token) }
    }

    override fun getAuthToken(): String? {
        return localDataSource.getString(DataConstant.AUTH_TOKEN_PREF_KEY, null)
    }

    override fun clearAuthToken() {
        localDataSource.edit { remove(DataConstant.AUTH_TOKEN_PREF_KEY) }
    }
}
