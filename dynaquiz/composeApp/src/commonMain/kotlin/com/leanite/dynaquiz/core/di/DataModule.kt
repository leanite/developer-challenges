package com.leanite.dynaquiz.core.di

import com.leanite.dynaquiz.config.BuildKonfig
import com.leanite.dynaquiz.core.data.database.ChallengeModeAdapter
import com.leanite.dynaquiz.core.data.database.DatabaseDriverFactory
import com.leanite.dynaquiz.core.data.datasource.ChallengeModeLocalDataSource
import com.leanite.dynaquiz.core.data.datasource.ChallengeModeLocalDataSourceImpl
import com.leanite.dynaquiz.core.data.datasource.PlayerLocalDataSource
import com.leanite.dynaquiz.core.data.datasource.PlayerLocalDataSourceImpl
import com.leanite.dynaquiz.core.data.datasource.QuizRemoteDataSource
import com.leanite.dynaquiz.core.data.datasource.QuizRemoteDataSourceImpl
import com.leanite.dynaquiz.core.data.datasource.QuizSessionLocalDataSource
import com.leanite.dynaquiz.core.data.datasource.QuizSessionLocalDataSourceImpl
import com.leanite.dynaquiz.core.data.repository.ChallengeModeRepositoryImpl
import com.leanite.dynaquiz.core.data.repository.DatabaseRepositoryImpl
import com.leanite.dynaquiz.core.data.repository.PlayerRepositoryImpl
import com.leanite.dynaquiz.core.data.repository.QuizRepositoryImpl
import com.leanite.dynaquiz.core.data.repository.RankingRepositoryImpl
import com.leanite.dynaquiz.core.data.repository.UserRepositoryImpl
import com.leanite.dynaquiz.core.domain.repository.ChallengeModeRepository
import com.leanite.dynaquiz.core.domain.repository.DatabaseRepository
import com.leanite.dynaquiz.core.domain.repository.PlayerRepository
import com.leanite.dynaquiz.core.domain.repository.QuizRepository
import com.leanite.dynaquiz.core.domain.repository.RankingRepository
import com.leanite.dynaquiz.core.domain.repository.UserRepository
import com.leanite.dynaquiz.database.DynaquizDatabase
import com.leanite.dynaquiz.database.QuizSessionEntity
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule =
    module {
        // Banco local (SQLDelight)
        single<DynaquizDatabase> {
            DynaquizDatabase(
                driver = get<DatabaseDriverFactory>().create(),
                // Adapter pra coluna challengeMode de TEXT para class ChallengeMode
                QuizSessionEntityAdapter =
                    QuizSessionEntity.Adapter(
                        challengeModeAdapter = ChallengeModeAdapter,
                    ),
            )
        }

        // Data sources
        single<PlayerLocalDataSource> { PlayerLocalDataSourceImpl(database = get()) }
        single<QuizRemoteDataSource> {
            QuizRemoteDataSourceImpl(
                httpClient = get(),
                baseUrl = BuildKonfig.DYNAMOX_QUIZ_BASE_URL,
                jsonConfig = get(),
            )
        }
        single<ChallengeModeLocalDataSource> { ChallengeModeLocalDataSourceImpl(settings = get()) }
        single<QuizSessionLocalDataSource> { QuizSessionLocalDataSourceImpl(database = get()) }

        // Repositories
        single<PlayerRepository> {
            PlayerRepositoryImpl(
                localDataSource = get(),
                clock = get(),
                ioDispatcher = get(named("io")),
            )
        }
        single<QuizRepository> {
            QuizRepositoryImpl(
                remoteDataSource = get(),
                ioDispatcher = get(named("io")),
            )
        }
        single<UserRepository> { UserRepositoryImpl(settings = get()) }
        single<ChallengeModeRepository> { ChallengeModeRepositoryImpl(dataSource = get()) }
        single<RankingRepository> {
            RankingRepositoryImpl(
                quizSessionDataSource = get(),
                playerDataSource = get(),
                clock = get(),
                ioDispatcher = get(named("io")),
            )
        }
        single<DatabaseRepository> {
            DatabaseRepositoryImpl(
                databaseProvider = { get<DynaquizDatabase>() },
                ioDispatcher = get(named("io")),
            )
        }
    }
