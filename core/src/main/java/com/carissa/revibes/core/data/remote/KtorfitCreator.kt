package com.carissa.revibes.core.data.remote

import com.carissa.revibes.core.data.remote.okhttp.ClientCreator
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
internal class KtorfitCreator(
    private val baseUrlProvider: BaseUrlProvider,
    private val okhttpClientCreator: ClientCreator,
    private val apiErrorHandler: ApiErrorHandler,
    private val apiErrorValidator: ApiErrorValidator,
    private val apiHttpLogger: ApiHttpLogger,
    private val json: Json
) {
    fun create(): Ktorfit {
        return Ktorfit.Builder()
            .httpClient(OkHttp) {
                engine {
                    preconfigured = okhttpClientCreator.create()
                }
                install(ContentNegotiation) {
                    json(json)
                }
                expectSuccess = true

                install(HttpCallValidator) {
                    apiErrorHandler.configure(this)

                    validateResponse { response ->
                        apiErrorValidator.validate(response)
                    }
                }
                install(Logging) {
                    logger = apiHttpLogger
                    level = LogLevel.ALL
                }
            }
            .baseUrl(baseUrlProvider.getBaseUrl())
            .build()
    }
}
