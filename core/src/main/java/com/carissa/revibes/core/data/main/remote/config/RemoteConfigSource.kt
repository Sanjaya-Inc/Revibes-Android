package com.carissa.revibes.core.data.main.remote.config

/**
 * An interface for fetching key-value pairs from a remote configuration source.
 * This abstraction allows swapping the implementation (e.g., Firebase, custom API)
 * without changing the repository.
 */
interface RemoteConfigSource {
    /**
     * Fetches and activates the latest remote config values.
     * Throws an exception on failure.
     */
    suspend fun fetchAndActivate()

    fun getString(key: String): String
    fun getBoolean(key: String): Boolean
    fun getLong(key: String): Long
    fun getDouble(key: String): Double
}
