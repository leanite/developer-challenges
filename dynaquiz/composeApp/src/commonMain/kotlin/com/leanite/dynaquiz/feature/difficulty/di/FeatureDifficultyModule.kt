package com.leanite.dynaquiz.feature.difficulty.di

import com.leanite.dynaquiz.feature.difficulty.DifficultyViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureDifficultyModule = module {
    viewModelOf(::DifficultyViewModel)
}