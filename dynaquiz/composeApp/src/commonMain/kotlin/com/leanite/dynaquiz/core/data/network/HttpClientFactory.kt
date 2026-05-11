package com.leanite.dynaquiz.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

internal fun buildHttpClient(): HttpClient = HttpClient(httpClientEngine()) {
    install(ContentNegotiation) {
        json(DynaquizJson)
    }
    install(Logging) {
        level = LogLevel.ALL
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 15_000
        connectTimeoutMillis = 10_000
    }
}