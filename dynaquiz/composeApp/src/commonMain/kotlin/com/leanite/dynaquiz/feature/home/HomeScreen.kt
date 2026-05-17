package com.leanite.dynaquiz.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.ui.common.GameBackground
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.home.ui.BackgroundGradient
import com.leanite.dynaquiz.feature.home.ui.MainMenu
import com.leanite.dynaquiz.feature.home.ui.ProfileCard

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        GameBackground(modifier = Modifier.matchParentSize())
        BackgroundGradient(modifier = Modifier.matchParentSize())

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileCard(
                nickname = uiState.nickname,
                mascot = uiState.challengeMode.mascot,
                onNicknameChange = { onIntent(HomeIntent.NicknameChanged(it.trim().uppercase())) },
            )

            Spacer(modifier = Modifier.weight(1f))

            MainMenu(
                isStartEnabled = uiState.canStart,
                onStartClick = { onIntent(HomeIntent.StartQuizClicked) },
                onDifficultyClick = { onIntent(HomeIntent.DifficultyClicked) },
                onRankingClick = { onIntent(HomeIntent.RankingClicked) },
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() { // TODO: criar Preview default incorporando ja o DynaquizTheme por ext fun
    DynaquizTheme {
        HomeScreen(
            HomeUiState(
                nickname = "Leandro",
            ),
            onIntent = {},
        )
    }
}
