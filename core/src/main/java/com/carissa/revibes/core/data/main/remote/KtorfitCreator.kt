package com.carissa.revibes.core.data.main.remote

import com.carissa.revibes.core.data.main.remote.okhttp.ClientCreator
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
internal class KtorfitCreator(
    private val baseUrlProvider: BaseUrlProvider,
    private val okhttpClientCreator: ClientCreator,
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
                    json(json, contentType = ContentType.Application.Json)
                }
                expectSuccess = false
                HttpResponseValidator {
                    validateResponse { response ->
                        if (response.status.value !in 200..299) {
                            val errorText = response.bodyAsText()
                            throw ResponseException(response, errorText)
                        }
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
