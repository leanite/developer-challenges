package com.leanite.dynaquiz

import androidx.compose.ui.window.ComposeUIViewController
import com.leanite.dynaquiz.core.di.initKoin
import com.leanite.dynaquiz.core.di.iosModule
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin(extraModules = listOf(iosModule))
    return ComposeUIViewController { App() }
}