package com.leanite.dynaquiz.feature.result.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.core.ui.common.label
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.result.res.ResultRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun FinalScorePanel(
    result: QuizSessionResult,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(20.dp),
                ).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MascotWinAnimation(
            mascotMood = result.challengeMode.mascot.mood,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(ResultRes.Congrats, result.playerName),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = result.score.points.toString(),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = stringResource(ResultRes.PointsLabel),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =
                stringResource(
                    ResultRes.Details,
                    result.correctAnswers,
                    result.totalQuestions,
                    result.challengeMode.label(),
                ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun FinalScorePanelPreview() {
    DynaquizTheme {
        FinalScorePanel(
            result =
                QuizSessionResult(
                    playerName = "Leandro",
                    challengeMode = ChallengeMode.Timed.Medium,
                    score = Score(180),
                    correctAnswers = 8,
                    totalQuestions = 10,
                ),
        )
    }
}
