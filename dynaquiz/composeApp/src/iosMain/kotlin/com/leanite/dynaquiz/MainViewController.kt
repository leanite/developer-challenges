package com.leanite.dynaquiz

import androidx.compose.ui.window.ComposeUIViewController
import com.leanite.dynaquiz.core.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }
}