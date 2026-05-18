package com.leanite.dynaquiz.core.di

import com.leanite.dynaquiz.core.domain.usecase.GetLastChallengeModeUseCase
import com.leanite.dynaquiz.core.domain.usecase.GetLastNicknameUseCase
import com.leanite.dynaquiz.core.domain.usecase.GetMyRankingUseCase
import com.leanite.dynaquiz.core.domain.usecase.GetRandomQuestionUseCase
import com.leanite.dynaquiz.core.domain.usecase.GetRankingUseCase
import com.leanite.dynaquiz.core.domain.usecase.RegisterOrFetchPlayerUseCase
import com.leanite.dynaquiz.core.domain.usecase.SaveQuizSessionUseCase
import com.leanite.dynaquiz.core.domain.usecase.SetLastChallengeModeUseCase
import com.leanite.dynaquiz.core.domain.usecase.SetLastNicknameUseCase
import com.leanite.dynaquiz.core.domain.usecase.SubmitAnswerUseCase
import com.leanite.dynaquiz.core.domain.usecase.WarmupDatabaseUseCase
import com.leanite.dynaquiz.core.domain.usecase.WarmupServerUseCase
import org.koin.dsl.module

val domainModule =
    module {
        // Splash
        factory { WarmupServerUseCase(repository = get()) }
        factory { WarmupDatabaseUseCase(repository = get()) }

        // Home
        factory { GetLastNicknameUseCase(repository = get()) }
        factory { SetLastNicknameUseCase(repository = get()) }
        factory { RegisterOrFetchPlayerUseCase(repository = get()) }

        // Home + Difficulty (shared)
        factory { GetLastChallengeModeUseCase(repository = get()) }

        // Difficulty
        factory { SetLastChallengeModeUseCase(repository = get()) }

        // Quiz
        factory { GetRandomQuestionUseCase(repository = get()) }
        factory { SubmitAnswerUseCase(repository = get()) }
        factory { SaveQuizSessionUseCase(repository = get()) }

        // Ranking
        factory { GetRankingUseCase(repository = get()) }
        factory { GetMyRankingUseCase(repository = get()) }
    }
