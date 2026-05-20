package com.leanite.dynaquiz.feature.quiz

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.QuizSetup
import com.leanite.dynaquiz.core.ui.common.BackHandler
import com.leanite.dynaquiz.core.ui.common.DynaquizTopBar
import com.leanite.dynaquiz.feature.quiz.res.QuizRes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun QuizHost(
    setup: QuizSetup,
    onNavigateToResult: (result: QuizSessionResult) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val viewModel: QuizViewModel =
        koinViewModel {
            parametersOf(setup)
        }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    QuizIntentEffects(onIntent = viewModel::onIntent)

    QuizEventEffects(
        events = viewModel.events,
        snackbarHostState = snackbarHostState,
        onNavigateToResult = onNavigateToResult,
        onNavigateBack = onNavigateBack,
    )

    // Quando o dialog já estiver aberto, não intercepta
    // deixa o back nativo fechar o dialog (dismissOnBackPress = true)
    BackHandler(enabled = !uiState.showExitDialog) {
        viewModel.onIntent(QuizIntent.BackPressed)
    }

    Scaffold(
        topBar = {
            DynaquizTopBar(
                title = {
                    Text(
                        text =
                            stringResource(
                                QuizRes.TopBarProgress,
                                uiState.questionNumber,
                                QuizRules.TOTAL_QUESTIONS,
                            ),
                    )
                },
                showNavigation = true,
                onNavigationClick = { viewModel.onIntent(QuizIntent.BackPressed) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        QuizScreen(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        )
    }
}
