package com.leanite.dynaquiz.feature.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.ui.common.DynaquizTopBar
import com.leanite.dynaquiz.feature.result.res.ResultRes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ResultHost(
    sessionResult: QuizSessionResult,
    onNavigateToHome: () -> Unit,
    onNavigateToRanking: (String) -> Unit,
) {
    val viewModel: ResultViewModel = koinViewModel { parametersOf(sessionResult) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ResultEventEffects(
        events = viewModel.events,
        onNavigateToHome = onNavigateToHome,
        onNavigateToRanking = onNavigateToRanking,
    )

    Scaffold(
        topBar = {
            DynaquizTopBar(
                title = { Text(stringResource(ResultRes.Title)) },
            )
        },
    ) { innerPadding ->
        ResultScreen(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        )
    }
}