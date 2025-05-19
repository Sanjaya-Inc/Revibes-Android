package com.carissa.revibes.core.data.util

import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
internal class JsonParserCreator {
    fun create(): Json = Json {
        prettyPrint = true
        isLenient = false
        ignoreUnknownKeys = true
    }
}
