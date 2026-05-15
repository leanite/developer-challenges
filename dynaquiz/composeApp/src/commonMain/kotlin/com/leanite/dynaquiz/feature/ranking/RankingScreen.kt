package com.leanite.dynaquiz.feature.ranking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leanite.dynaquiz.core.ui.common.GameBackground
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.ranking.ui.EmptyRankingState
import com.leanite.dynaquiz.feature.ranking.ui.RankingList
import com.leanite.dynaquiz.feature.ranking.ui.RankingTabBar

@Composable
fun RankingScreen(
    uiState: RankingUiState,
    onIntent: (RankingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        GameBackground(modifier = Modifier.matchParentSize())

        Column(modifier = Modifier.fillMaxSize()) {
            RankingTabBar(
                selectedTab = uiState.selectedTab,
                onTabSelected = { onIntent(RankingIntent.TabSelected(it)) },
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.align(Alignment.Center),
                    )

                    uiState.entries.isEmpty() -> EmptyRankingState(
                        tab = uiState.selectedTab,
                        modifier = Modifier.align(Alignment.Center),
                    )

                    else -> RankingList(entries = uiState.entries)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RankingScreenPreview() {
    DynaquizTheme {
        RankingScreen(
            uiState = RankingUiState(),
            onIntent = {}
        )
    }
}