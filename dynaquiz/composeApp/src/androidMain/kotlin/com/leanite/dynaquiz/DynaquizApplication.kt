package com.leanite.dynaquiz

import android.app.Application
import com.leanite.dynaquiz.core.di.androidModule
import com.leanite.dynaquiz.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class DynaquizApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            extraModules = listOf(androidModule)
        ) {
            androidContext(this@DynaquizApplication)
        }
    }
}