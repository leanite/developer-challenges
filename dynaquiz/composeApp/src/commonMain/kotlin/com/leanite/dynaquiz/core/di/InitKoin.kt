package com.leanite.dynaquiz.core.di

import com.leanite.dynaquiz.feature.difficulty.di.featureDifficultyModule
import com.leanite.dynaquiz.feature.home.di.featureHomeModule
import com.leanite.dynaquiz.feature.quiz.di.featureQuizModule
import com.leanite.dynaquiz.feature.ranking.di.featureRankingModule
import com.leanite.dynaquiz.feature.result.di.featureResultModule
import com.leanite.dynaquiz.feature.splash.di.featureSplashModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.mp.KoinPlatformTools

fun initKoin(
    extraModules: List<Module> = emptyList(),
    appDeclaration: KoinApplication.() -> Unit = {},
) {
    if (KoinPlatformTools.defaultContext().getOrNull() != null) return

    startKoin {
        appDeclaration()
        modules(
            listOf(
                coreModule,
                dataModule,
                domainModule,
                featureSplashModule,
                featureHomeModule,
                featureDifficultyModule,
                featureQuizModule,
                featureResultModule,
                featureRankingModule,
            ) + extraModules,
        )
    }
}
