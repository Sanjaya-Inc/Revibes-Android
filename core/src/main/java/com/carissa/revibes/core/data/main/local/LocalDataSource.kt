package com.carissa.revibes.core.data.main.local

import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import org.koin.core.annotation.Single

interface LocalDataSource : SharedPreferences

@Single(binds = [LocalDataSource::class])
internal class LocalDataSourceImpl(
    private val sharedPreferences: SharedPreferences = MMKV.defaultMMKV()
) : LocalDataSource, SharedPreferences by sharedPreferences
