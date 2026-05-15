package com.leanite.dynaquiz.feature.difficulty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.ui.common.GameBackground
import com.leanite.dynaquiz.core.ui.common.GeneralActionButton
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.difficulty.ui.ChallengeModeOption // reaproveitando o existente
import com.leanite.dynaquiz.feature.difficulty.res.DifficultyRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun DifficultyScreen(
    uiState: DifficultyUiState,
    onIntent: (DifficultyIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        GameBackground(modifier = Modifier.matchParentSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ChallengeModeOption(
                    label = stringResource(DifficultyRes.ModeRelaxedLabel),
                    description = stringResource(DifficultyRes.ModeRelaxedDescription),
                    mode = ChallengeMode.Relaxed,
                    isSelected = uiState.selectedMode == ChallengeMode.Relaxed,
                    onSelect = { onIntent(DifficultyIntent.ModeSelected(it)) },
                )
                ChallengeModeOption(
                    label = stringResource(DifficultyRes.ModeEasyLabel),
                    description = stringResource(DifficultyRes.ModeEasyDescription),
                    mode = ChallengeMode.Timed.Easy,
                    isSelected = uiState.selectedMode == ChallengeMode.Timed.Easy,
                    onSelect = { onIntent(DifficultyIntent.ModeSelected(it)) },
                )
                ChallengeModeOption(
                    label = stringResource(DifficultyRes.ModeMediumLabel),
                    description = stringResource(DifficultyRes.ModeMediumDescription),
                    mode = ChallengeMode.Timed.Medium,
                    isSelected = uiState.selectedMode == ChallengeMode.Timed.Medium,
                    onSelect = { onIntent(DifficultyIntent.ModeSelected(it)) },
                )
                ChallengeModeOption(
                    label = stringResource(DifficultyRes.ModeHardLabel),
                    description = stringResource(DifficultyRes.ModeHardDescription),
                    mode = ChallengeMode.Timed.Hard,
                    isSelected = uiState.selectedMode == ChallengeMode.Timed.Hard,
                    onSelect = { onIntent(DifficultyIntent.ModeSelected(it)) },
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            GeneralActionButton(
                text = stringResource(DifficultyRes.ButtonConfirm),
                onClick = { onIntent(DifficultyIntent.ConfirmClicked) },
                enabled = !uiState.isConfirming,
                modifier = Modifier.fillMaxWidth().padding(20.dp),
            )
        }
    }
}

@Preview
@Composable
private fun DifficultyScreenPreview() {
    DynaquizTheme {
        DifficultyScreen(
            uiState = DifficultyUiState(),
            onIntent = {}
        )
    }
}