package com.leanite.dynaquiz.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.feature.home.res.HomeRes
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
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = uiState.nickname,
            onValueChange = { onIntent(HomeIntent.NicknameChanged(it)) },
            label = { Text(stringResource(HomeRes.NicknameLabel)) },
            singleLine = true,
            isError = uiState.nicknameError != null && uiState.nickname.isNotEmpty(),
            supportingText = {
                if (uiState.nickname.isNotEmpty()) {
                    uiState.nicknameError?.let { error ->
                        Text(error.toUserMessage())
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(HomeRes.DifficultyTitle),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
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

@Composable
private fun ChallengeModeOption(
    label: String,
    description: String,
    mode: ChallengeMode,
    isSelected: Boolean,
    onSelect: (ChallengeMode) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = { onSelect(mode) },
                role = Role.RadioButton,
            )
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun NicknameError.toUserMessage(): String = when (this) {
    NicknameError.Empty -> stringResource(HomeRes.NicknameErrorEmpty)
    NicknameError.TooShort -> stringResource(
        HomeRes.NicknameErrorTooShort,
        HomeValidation.MIN_NICKNAME_LENGTH,
    )
    NicknameError.TooLong -> stringResource(
        HomeRes.NicknameErrorTooLong,
        HomeValidation.MAX_NICKNAME_LENGTH,
    )
}