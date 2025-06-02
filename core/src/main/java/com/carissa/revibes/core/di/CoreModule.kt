package com.carissa.revibes.core.di

import com.carissa.revibes.core.data.remote.KtorfitCreator
import com.carissa.revibes.core.data.util.JsonParserCreator
import de.jensklingenberg.ktorfit.Ktorfit
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan(
    "com.carissa.revibes.core.data",
    "com.carissa.revibes.core.domain",
    "com.carissa.revibes.core.presentation"
)
object CoreModule {

    @Single
    internal fun provideJson(
        creator: JsonParserCreator
    ): Json {
        return creator.create()
    }

    @Single
    internal fun provideKtorfit(
        creator: KtorfitCreator
    ): Ktorfit {
        return creator.create()
    }
}
