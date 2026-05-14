package com.leanite.dynaquiz.feature.difficulty

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leanite.dynaquiz.core.ext.usableString
import com.leanite.dynaquiz.core.ui.common.DynaquizTopBar
import com.leanite.dynaquiz.feature.difficulty.res.DifficultyRes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DifficultyHost(
    onNavigateBack: () -> Unit,
    viewModel: DifficultyViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DifficultyIntentEffects(onIntent = viewModel::onIntent)
    DifficultyEventEffects(
        events = viewModel.events,
        onNavigateBack = onNavigateBack,
    )

    Scaffold(
        topBar = {
            DynaquizTopBar(
                title = {
                    Text(
                        text = DifficultyRes.Title.usableString(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                showNavigation = true,
                onNavigationClick = {
                    viewModel.onIntent(DifficultyIntent.BackClicked)
                },
            )
        },
        containerColor = Color.Transparent,
    ) { innerPadding ->
        DifficultyScreen(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        )
    }
}