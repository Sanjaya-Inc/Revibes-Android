package com.carissa.revibes.core.data.main.remote.config

import com.carissa.revibes.core.R
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class FirebaseRemoteConfigSource(
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
) : RemoteConfigSource {

    init {
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override suspend fun fetchAndActivate() {
        remoteConfig.fetchAndActivate().await()
    }

    override fun getString(key: String): String = remoteConfig.getString(key)

    override fun getBoolean(key: String): Boolean = remoteConfig.getBoolean(key)

    override fun getLong(key: String): Long = remoteConfig.getLong(key)

    override fun getDouble(key: String): Double = remoteConfig.getDouble(key)
}
