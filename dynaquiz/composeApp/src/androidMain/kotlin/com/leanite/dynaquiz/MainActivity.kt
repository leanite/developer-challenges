package com.leanite.dynaquiz

import android.app.UiModeManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(UiModeManager.MODE_NIGHT_YES),
            navigationBarStyle = SystemBarStyle.dark(UiModeManager.MODE_NIGHT_YES),
        )
        setContent { App() }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}