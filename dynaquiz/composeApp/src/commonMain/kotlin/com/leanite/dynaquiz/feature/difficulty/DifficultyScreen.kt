package com.leanite.dynaquiz.feature.difficulty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.toMascot
import com.leanite.dynaquiz.core.ui.common.GameBackground
import com.leanite.dynaquiz.core.ui.common.GameButton
import com.leanite.dynaquiz.core.ui.common.GameButtonStyle
import com.leanite.dynaquiz.core.ui.common.MascotImage
import com.leanite.dynaquiz.feature.home.ui.ChallengeModeOption // reaproveitando o existente
import com.leanite.dynaquiz.feature.difficulty.res.DifficultyRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun DifficultyScreen(
    uiState: DifficultyUiState,
    onIntent: (DifficultyIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    GameBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MascotImage(
                mascot = uiState.selectedMode.toMascot(),
                modifier = Modifier.size(140.dp),
            )

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = stringResource(DifficultyRes.Title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.size(24.dp))

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

            GameButton(
                text = stringResource(DifficultyRes.ButtonConfirm),
                onClick = { onIntent(DifficultyIntent.ConfirmClicked) },
                style = GameButtonStyle.Primary,
                enabled = !uiState.isConfirming,
                modifier = Modifier.fillMaxWidth(0.7f),
                leadingContent = if (uiState.isConfirming) {
                    {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onSecondary,
                            strokeWidth = 2.dp,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                } else null,
            )

            Spacer(modifier = Modifier.size(12.dp))

            GameButton(
                text = stringResource(DifficultyRes.ButtonBack),
                onClick = { onIntent(DifficultyIntent.BackClicked) },
                style = GameButtonStyle.Secondary,
                modifier = Modifier.fillMaxWidth(0.7f),
            )
        }
    }
}