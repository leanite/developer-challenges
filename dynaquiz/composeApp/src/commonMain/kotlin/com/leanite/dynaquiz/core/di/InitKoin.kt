package com.leanite.dynaquiz.core.di

import com.leanite.dynaquiz.feature.home.di.featureHomeModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.mp.KoinPlatformTools

fun initKoin(
    extraModules: List<Module> = emptyList(),
    appDeclaration: KoinApplication.() -> Unit = {}
) {
    if (KoinPlatformTools.defaultContext().getOrNull() != null) return

    startKoin {
        appDeclaration()
        modules(
            listOf(
                coreModule,
                featureHomeModule
            ) + extraModules)
    }
}