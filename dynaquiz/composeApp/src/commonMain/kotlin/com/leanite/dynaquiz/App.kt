package com.leanite.dynaquiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.repository.QuizRepository
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.splash.SplashHost
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    // KoinContext "expõe" o Koin global (iniciado em DynaquizApplication
    // no Android e em MainViewController no iOS) ao Compose tree, de modo
    // que `koinInject<T>()` funcione em qualquer composable descendente.
    // NÃO inicia Koin — só consome o que já foi iniciado.
    KoinContext {
        DynaquizTheme {
            var splashFinished by remember { mutableStateOf(false) }

            if (!splashFinished) {
                SplashHost(onNavigateToNext = { splashFinished = true })
            } else {
                DebugHomeScreen()
            }
        }
    }
}

/**
 * Tela placeholder de DEBUG — agora consome [QuizRepository] via Koin.
 * Sai quando a feature Home real (input de nickname) entrar.
 */
@Composable
private fun DebugHomeScreen() {
    val scope = rememberCoroutineScope()
    val repository: QuizRepository = koinInject()

    var result: AppResult<Question>? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text("Debug — fetch pergunta", style = MaterialTheme.typography.headlineSmall)

        Button(
            enabled = !isLoading,
            onClick = {
                scope.launch {
                    isLoading = true
                    result = repository.getRandomQuestion()
                    isLoading = false
                }
            },
        ) {
            Text(if (isLoading) "Carregando..." else "Buscar pergunta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val r = result) {
            is AppResult.Success -> {
                Text("ID: ${r.data.id.value}", style = MaterialTheme.typography.titleSmall)
                Text(r.data.statement, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                r.data.options.forEach { option ->
                    Text("• $option", style = MaterialTheme.typography.bodyMedium)
                }
            }
            is AppResult.Error -> {
                Text(
                    "Erro: ${r.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            null -> {
                Text(
                    "Clique para buscar uma pergunta da Dynamox",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}