package com.leanite.dynaquiz.feature.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.ui.common.GameBackground
import com.leanite.dynaquiz.core.ui.common.GeneralActionButton
import com.leanite.dynaquiz.core.ui.common.GeneralActionButtonStyle
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.result.res.ResultRes
import com.leanite.dynaquiz.feature.result.ui.FinalScorePanel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResultScreen(
    uiState: ResultUiState,
    onIntent: (ResultIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        GameBackground(Modifier.matchParentSize())

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            FinalScorePanel(
                result = uiState.result,
                modifier = Modifier.fillMaxWidth(0.85f),
            )

            Spacer(modifier = Modifier.weight(1f))

            GeneralActionButton(
                text = stringResource(ResultRes.ButtonRanking),
                onClick = { onIntent(ResultIntent.RankingClicked) },
            )
            Spacer(modifier = Modifier.height(12.dp))
            GeneralActionButton(
                text = stringResource(ResultRes.ButtonHome),
                onClick = { onIntent(ResultIntent.HomeClicked) },
                style = GeneralActionButtonStyle.Secondary,
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
private fun ResultScreenPreview() {
    DynaquizTheme {
        ResultScreen(
            uiState =
                ResultUiState(
                    result =
                        QuizSessionResult(
                            playerName = "NAME",
                            challengeMode = ChallengeMode.Timed.Easy,
                            score = Score(100),
                            correctAnswers = 5,
                            totalQuestions = 10,
                        ),
                ),
            onIntent = {},
        )
    }
}
