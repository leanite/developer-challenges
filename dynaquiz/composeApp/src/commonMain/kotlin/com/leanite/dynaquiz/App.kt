package com.leanite.dynaquiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.splash.SplashHost

@Composable
@Preview
fun App() {
    DynaquizTheme {
        var splashFinished by remember { mutableStateOf(false) }

        if (!splashFinished) {
            SplashHost(
                onNavigateToNext = { splashFinished = true }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .safeContentPadding(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Home (placeholder)", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}