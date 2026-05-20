package com.leanite.dynaquiz.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leanite.dynaquiz.core.domain.model.QuizSetup
import com.leanite.dynaquiz.core.ui.common.BrandTitle
import com.leanite.dynaquiz.core.ui.common.BrandTitleSize
import com.leanite.dynaquiz.core.ui.common.DynaquizTopBar
import com.leanite.dynaquiz.feature.home.res.HomeRes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHost(
    onNavigateToQuiz: (QuizSetup) -> Unit,
    onNavigateToDifficulty: () -> Unit,
    onNavigateToRanking: (String) -> Unit,
    titleModifier: Modifier = Modifier,
    purpleSurfaceModifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    HomeIntentEffects(onIntent = viewModel::onIntent)
    HomeEventEffects(
        events = viewModel.events,
        snackbarHostState = snackbarHostState,
        onNavigateToQuiz = onNavigateToQuiz,
        onNavigateToDifficulty = onNavigateToDifficulty,
        onNavigateToRanking = onNavigateToRanking,
    )

    Scaffold(
        topBar = {
            DynaquizTopBar(
                modifier = purpleSurfaceModifier,
                title = {
                    BrandTitle(
                        text = stringResource(HomeRes.Title),
                        size = BrandTitleSize.NORMAL,
                        modifier = titleModifier,
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        HomeScreen(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        )
    }
}
