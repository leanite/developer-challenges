package com.leanite.dynaquiz.feature.splash.di

import com.leanite.dynaquiz.feature.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureSplashModule = module {
    viewModel { SplashViewModel() }
}