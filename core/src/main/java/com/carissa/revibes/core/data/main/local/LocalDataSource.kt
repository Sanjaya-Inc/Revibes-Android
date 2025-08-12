package com.carissa.revibes.core.data.main.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.tencent.mmkv.MMKV
import org.koin.core.annotation.Single

interface LocalDataSource : SharedPreferences {
    companion object {
        var isMMKVSuccesfullyInitialize = false

        @SuppressLint("ObsoleteSdkInt")
        private fun is64BitArchitecture(): Boolean {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    Build.SUPPORTED_64_BIT_ABIS.isNotEmpty()
                }
                else -> {
                    val architecture = System.getProperty("os.arch") ?: ""
                    architecture.contains("64") ||
                        architecture.contains("aarch64") ||
                        architecture.contains("x86_64")
                }
            }
        }

        fun maybeInitMMKV(context: Context) = runCatching {
            if (is64BitArchitecture()) {
                MMKV.initialize(context)
                isMMKVSuccesfullyInitialize = true
            }
        }.onFailure {
            isMMKVSuccesfullyInitialize = false
            Log.i(TAG, "Failed to initialize MMKV ")
        }

        fun maybeUseMMKV(context: Context): SharedPreferences {
            return runCatching {
                if (isMMKVSuccesfullyInitialize) {
                    MMKV.defaultMMKV()
                } else {
                    createSharedPref(context)
                }
            }.getOrDefault(createSharedPref(context))
        }

        private fun createSharedPref(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        private const val TAG = "LocalDataSource"
        private const val PREF_NAME = "revibes_preferences"
    }
}

@Single(binds = [LocalDataSource::class])
internal class LocalDataSourceImpl(
    private val context: Context
) : LocalDataSource, SharedPreferences by LocalDataSource.maybeUseMMKV(context)
