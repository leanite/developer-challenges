package com.leanite.dynaquiz.feature.result.di

import com.leanite.dynaquiz.feature.result.ResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureResultModule =
    module {
        viewModel { params ->
            ResultViewModel(sessionResult = params.get())
        }
    }
