package com.carissa.revibes.core.data.local

import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import org.koin.core.annotation.Single

interface LocalPrefGateway : SharedPreferences

@Single(binds = [LocalPrefGateway::class])
internal class LocalPrefGatewayImpl(
    private val sharedPreferences: SharedPreferences = MMKV.defaultMMKV()
) : LocalPrefGateway, SharedPreferences by sharedPreferences
