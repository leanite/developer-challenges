package com.leanite.dynaquiz

import android.app.Application
import com.leanite.dynaquiz.core.di.initKoin

class DynaquizApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}