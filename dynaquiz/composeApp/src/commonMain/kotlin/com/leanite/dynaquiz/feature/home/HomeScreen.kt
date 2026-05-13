package com.leanite.dynaquiz.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.Mascot
import com.leanite.dynaquiz.core.domain.model.toMascot
import com.leanite.dynaquiz.core.ui.common.GameBackground
import com.leanite.dynaquiz.core.ui.common.GameButton
import com.leanite.dynaquiz.core.ui.common.GameButtonStyle
import com.leanite.dynaquiz.core.ui.common.MascotImage
import com.leanite.dynaquiz.core.ui.theme.DynamoxPurple
import com.leanite.dynaquiz.core.ui.theme.DynamoxPurpleDeep
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.core.ui.theme.MascotYellow
import com.leanite.dynaquiz.core.ui.theme.MascotYellowDeep
import com.leanite.dynaquiz.feature.home.res.HomeRes
import com.leanite.dynaquiz.feature.home.ui.MainMenu
import com.leanite.dynaquiz.feature.home.ui.ProfileCard
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    GameBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileCard(
                nickname = uiState.nickname,
                mascot = uiState.challengeMode.toMascot(),
                onNicknameChange = { onIntent(HomeIntent.NicknameChanged(it.uppercase())) },
            )

            Spacer(modifier = Modifier.weight(1f))

            MainMenu(
                isStartEnabled = uiState.canStart,
                onStartClick = { onIntent(HomeIntent.StartQuizClicked) },
                onDifficultyClick = {},
                onRankingClick = { onIntent(HomeIntent.RankingClicked) }
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() { //TODO: criar Preview default incorporando ja o DynaquizTheme por ext fun
    DynaquizTheme {
        HomeScreen(
            HomeUiState(
                nickname = "Leandro",
            ),
            onIntent = {}
        )
    }
}