package com.leanite.dynaquiz.feature.home.di

import com.leanite.dynaquiz.feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureHomeModule = module {
    viewModel {
        HomeViewModel(
            getLastNicknameUseCase = get(),
            setLastNicknameUseCase = get(),
            registerOrFetchPlayerUseCase = get(),
        )
    }
}