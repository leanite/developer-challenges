package com.leanite.dynaquiz.core.di

import com.leanite.dynaquiz.core.data.database.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
    single<DatabaseDriverFactory> { DatabaseDriverFactory(androidContext()) }
}