package com.leanite.dynaquiz.core.di

import com.leanite.dynaquiz.core.data.database.DatabaseDriverFactory
import org.koin.dsl.module

val iosModule =
    module {
        single<DatabaseDriverFactory> { DatabaseDriverFactory() }
    }
