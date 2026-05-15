package com.leanite.dynaquiz.feature.ranking.di

import com.leanite.dynaquiz.feature.ranking.RankingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureRankingModule = module {
    viewModel { params ->
        RankingViewModel(
            playerName = params.get(),
            getRankingUseCase = get(),
            getMyRankingUseCase = get(),
        )
    }
}