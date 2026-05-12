package com.leanite.dynaquiz.core.di

import com.leanite.dynaquiz.config.BuildKonfig
import com.leanite.dynaquiz.core.data.database.ChallengeModeAdapter
import com.leanite.dynaquiz.core.data.database.DatabaseDriverFactory
import com.leanite.dynaquiz.core.data.datasource.PlayerLocalDataSource
import com.leanite.dynaquiz.core.data.datasource.PlayerLocalDataSourceImpl
import com.leanite.dynaquiz.core.data.datasource.QuizRemoteDataSource
import com.leanite.dynaquiz.core.data.datasource.QuizRemoteDataSourceImpl
import com.leanite.dynaquiz.core.data.network.buildHttpClient
import com.leanite.dynaquiz.core.data.repository.PlayerRepositoryImpl
import com.leanite.dynaquiz.core.data.repository.QuizRepositoryImpl
import com.leanite.dynaquiz.core.data.repository.UserRepositoryImpl
import com.leanite.dynaquiz.core.domain.repository.PlayerRepository
import com.leanite.dynaquiz.core.domain.repository.QuizRepository
import com.leanite.dynaquiz.core.domain.repository.UserRepository
import com.leanite.dynaquiz.core.domain.usecase.GetLastNicknameUseCase
import com.leanite.dynaquiz.core.domain.usecase.RegisterOrFetchPlayerUseCase
import com.leanite.dynaquiz.core.domain.usecase.SetLastNicknameUseCase
import com.leanite.dynaquiz.database.DynaquizDatabase
import com.leanite.dynaquiz.database.QuizSessionEntity
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Clock

val coreModule = module { //TODO: melhorar, todos estao aqui
    // Clock
    single<Clock> { Clock.System }

    // Serialization
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    // Multiplatform Settings
    single<Settings> {
        Settings()
    }

    // Dispatchers
    single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
    single<CoroutineDispatcher>(named("default")) { Dispatchers.Default }

    // Banco local (SQLDelight)
    single<DynaquizDatabase> {
        DynaquizDatabase(
            driver = get<DatabaseDriverFactory>().create(),
            // Adapter pra coluna challengeMode de TEXT para class ChallengeMode
            QuizSessionEntityAdapter = QuizSessionEntity.Adapter(
                challengeModeAdapter = ChallengeModeAdapter,
            ),
        )
    }

    // Http Client
    single<HttpClient> { buildHttpClient(jsonConfig = get()) }

    // Data layer
    single<PlayerLocalDataSource> { PlayerLocalDataSourceImpl(database = get()) }
    single<PlayerRepository> {
        PlayerRepositoryImpl(
            localDataSource = get(),
            clock = get(),
            ioDispatcher = get(named("io")),
        )
    }

    single<QuizRemoteDataSource> {
        QuizRemoteDataSourceImpl(
            httpClient = get(),
            baseUrl = BuildKonfig.DYNAMOX_QUIZ_BASE_URL,
            jsonConfig = get(),
        )
    }
    single<QuizRepository> {
        QuizRepositoryImpl(
            remoteDataSource = get(),
            ioDispatcher = get(named("io")),
        )
    }

    single<UserRepository> {
        UserRepositoryImpl(
            settings = get()
        )
    }

    // Player use cases
    factory { GetLastNicknameUseCase(repository = get()) }
    factory { SetLastNicknameUseCase(repository = get()) }
    factory { RegisterOrFetchPlayerUseCase(repository = get()) }
}