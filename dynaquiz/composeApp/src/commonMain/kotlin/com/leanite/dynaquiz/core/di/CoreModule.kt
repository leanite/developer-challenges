package com.leanite.dynaquiz.core.di

import com.leanite.dynaquiz.core.data.network.buildHttpClient
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Clock

val coreModule =
    module {
        single<Clock> { Clock.System }

        single<Json> {
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        }

        single<Settings> { Settings() }

        single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
        single<CoroutineDispatcher>(named("default")) { Dispatchers.Default }

        single<HttpClient> { buildHttpClient(jsonConfig = get()) }
    }
