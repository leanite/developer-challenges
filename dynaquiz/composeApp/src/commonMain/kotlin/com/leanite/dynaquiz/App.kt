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
import com.leanite.dynaquiz.config.BuildKonfig
import com.leanite.dynaquiz.core.data.datasource.QuizRemoteDataSourceImpl
import com.leanite.dynaquiz.core.data.network.buildHttpClient
import com.leanite.dynaquiz.core.data.repository.QuizRepositoryImpl
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.repository.QuizRepository
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.splash.SplashHost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    DynaquizTheme {
        var splashFinished by remember { mutableStateOf(false) }

        if (!splashFinished) {
            SplashHost(onNavigateToNext = { splashFinished = true })
        } else {
            DebugHomeScreen()
        }
    }
}

/**
 * Tela placeholder de **DEBUG** — usada só pra validar que o data layer
 * está funcionando ponta a ponta (HTTP → JSON → DTO → mapper → AppResult).
 *
 * Instancia o HttpClient/DataSource/Repository **manualmente** porque
 * Koin ainda não foi configurado. Quando Koin entrar, isso vira:
 *
 *     val repository: QuizRepository = koinInject()
 *
 * E esta tela inteira é substituída pelo `HomeHost` real (input de
 * nickname + start), conforme PLAN §5.1.
 */
@Composable
private fun DebugHomeScreen() {
    val scope = rememberCoroutineScope()
    val repository: QuizRepository = remember {
        QuizRepositoryImpl(
            remoteDataSource = QuizRemoteDataSourceImpl(
                httpClient = buildHttpClient(),
                baseUrl = BuildKonfig.DYNAMOX_QUIZ_BASE_URL,
            ),
            ioDispatcher = Dispatchers.IO,
        )
    }

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
                Text(
                    text = "ID: ${r.data.id.value}",
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = r.data.statement,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                r.data.options.forEach { option ->
                    Text(
                        text = "• $option",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            is AppResult.Error -> {
                Text(
                    text = "Erro: ${r.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            null -> {
                Text(
                    text = "Clique para buscar uma pergunta da Dynamox",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}