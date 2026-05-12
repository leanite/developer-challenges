package com.leanite.dynaquiz.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.PlayerId
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeHost(
    onNavigateToQuiz: (PlayerId, ChallengeMode) -> Unit,
    onNavigateToRanking: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    HomeIntentEffects(onIntent = viewModel::onIntent)
    HomeEventEffects(
        events = viewModel.events,
        snackbarHostState = snackbarHostState,
        onNavigateToQuiz = onNavigateToQuiz,
        onNavigateToRanking = onNavigateToRanking,
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, //TODO: temporário
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            HomeScreen(
                uiState = uiState,
                onIntent = viewModel::onIntent,
            )
        }
    }
}