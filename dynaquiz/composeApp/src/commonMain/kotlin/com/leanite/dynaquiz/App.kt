package com.leanite.dynaquiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.home.HomeHost
import com.leanite.dynaquiz.feature.splash.SplashHost
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    /* KoinContext "expõe" o Koin global (iniciado em DynaquizApplication
    ** no Android e em MainViewController no iOS) ao Compose tree, para que
    ** `koinInject<T>()` funcione em qualquer composable descendente
    ** Não inicia Koin, só consome o que já foi iniciado
    */
    KoinContext {
        DynaquizTheme {
            var splashFinished by remember { mutableStateOf(false) }

            if (!splashFinished) {
                SplashHost(onNavigateToNext = { splashFinished = true })
            } else {
                HomeHost(
                    onNavigateToQuiz = { _, _ -> /* todo: nav */ },
                    onNavigateToRanking = { /* todo: nav */ },
                )
            }
        }
    }
}
