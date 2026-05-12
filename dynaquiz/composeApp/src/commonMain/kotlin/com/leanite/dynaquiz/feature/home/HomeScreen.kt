package com.leanite.dynaquiz.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.feature.home.res.HomeRes
import com.leanite.dynaquiz.feature.home.ui.ChallengeModeOption
import com.leanite.dynaquiz.feature.home.ui.MainCard
import com.leanite.dynaquiz.feature.home.ui.NicknameField
import com.leanite.dynaquiz.feature.home.ui.RankingCard
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEFEDF1))
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MainCard(
            nickname = uiState.nickname,
            nicknameError = uiState.nicknameError,
            onNicknameChange = { onIntent(HomeIntent.NicknameChanged(it)) },
            onRankingClick = { onIntent(HomeIntent.RankingClicked) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(HomeRes.DifficultyTitle),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            ChallengeModeOption(
                label = stringResource(HomeRes.ModeRelaxedLabel),
                description = stringResource(HomeRes.ModeRelaxedDescription),
                mode = ChallengeMode.Relaxed,
                isSelected = uiState.challengeMode == ChallengeMode.Relaxed,
                onSelect = { onIntent(HomeIntent.ChallengeModeSelected(it)) },
            )
            ChallengeModeOption(
                label = stringResource(HomeRes.ModeEasyLabel),
                description = stringResource(HomeRes.ModeEasyDescription),
                mode = ChallengeMode.Timed.Easy,
                isSelected = uiState.challengeMode == ChallengeMode.Timed.Easy,
                onSelect = { onIntent(HomeIntent.ChallengeModeSelected(it)) },
            )
            ChallengeModeOption(
                label = stringResource(HomeRes.ModeMediumLabel),
                description = stringResource(HomeRes.ModeMediumDescription),
                mode = ChallengeMode.Timed.Medium,
                isSelected = uiState.challengeMode == ChallengeMode.Timed.Medium,
                onSelect = { onIntent(HomeIntent.ChallengeModeSelected(it)) },
            )
            ChallengeModeOption(
                label = stringResource(HomeRes.ModeHardLabel),
                description = stringResource(HomeRes.ModeHardDescription),
                mode = ChallengeMode.Timed.Hard,
                isSelected = uiState.challengeMode == ChallengeMode.Timed.Hard,
                onSelect = { onIntent(HomeIntent.ChallengeModeSelected(it)) },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onIntent(HomeIntent.StartQuizClicked) },
            enabled = uiState.canStart,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (uiState.isStarting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(stringResource(HomeRes.ButtonStart))
            }
        }
    }
}