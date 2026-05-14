package com.leanite.dynaquiz.feature.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuestionId
import com.leanite.dynaquiz.core.ui.common.GameBackground
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.quiz.ui.CountdownDisplay
import com.leanite.dynaquiz.feature.quiz.ui.ExitQuizDialog
import com.leanite.dynaquiz.feature.quiz.ui.QuestionCard
import com.leanite.dynaquiz.feature.quiz.ui.TimerRing

@Composable
fun QuizScreen(
    uiState: QuizUiState,
    onIntent: (QuizIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    GameBackground(modifier = modifier) {
        when (val phase = uiState.phase) {
            is QuizPhase.Countdown -> CountdownDisplay(
                secondsRemaining = phase.secondsRemaining,
            )

            QuizPhase.Loading -> LoadingPlaceholder()

            is QuizPhase.Playing -> PlayingContent(
                phase = phase,
                challengeMode = uiState.challengeMode,
                timeRemainingSec = uiState.timeRemainingSec,
                onOptionSelected = { onIntent(QuizIntent.AnswerSelected(it)) },
            )

            QuizPhase.Completed -> LoadingPlaceholder()
        }

        if (uiState.showExitDialog) {
            ExitQuizDialog(
                onConfirm = { onIntent(QuizIntent.ExitConfirmed) },
                onDismiss = { onIntent(QuizIntent.ExitCancelled) },
            )
        }
    }
}

@Composable
private fun PlayingContent(
    phase: QuizPhase.Playing,
    challengeMode: ChallengeMode,
    timeRemainingSec: Int?,
    onOptionSelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        if (challengeMode is ChallengeMode.Timed && timeRemainingSec != null) {
            TimerRing(
                timeRemainingSec = timeRemainingSec,
                totalSec = challengeMode.perQuestionSeconds,
                size = 88.dp,
                strokeWidth = 6.dp,
            )
        }

        QuestionCard(
            question = phase.question,
            selectedAnswer = phase.selectedAnswer,
            isSubmitting = phase.isSubmitting,
            onOptionSelected = onOptionSelected,
        )
    }
}

@Composable
private fun LoadingPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
    }
}

@Preview
@Composable
private fun QuizScreenPreview() {
    DynaquizTheme {
        QuizScreen(
            uiState = QuizUiState(
                timeRemainingSec = 10,
                phase = QuizPhase.Playing(
                    question =
                        Question(
                            QuestionId("48"),
                            "Xasss?",
                            listOf("A", "B", "C", "D", "E")
                        )
                ),
                challengeMode = ChallengeMode.Timed.Easy
            ),
            onIntent = {}
        )
    }
}