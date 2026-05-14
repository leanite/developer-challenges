package com.leanite.dynaquiz.feature.quiz

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.Score
import com.leanite.dynaquiz.feature.quiz.res.QuizRes
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.getString

@Composable
fun QuizEventEffects(
    events: Flow<QuizEvent>,
    snackbarHostState: SnackbarHostState,
    onNavigateToResult: (result: QuizSessionResult) -> Unit,
    onNavigateBack: () -> Unit,
) {
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is QuizEvent.NavigateToResult -> onNavigateToResult(event.result)

                QuizEvent.NavigateBack -> onNavigateBack()

                is QuizEvent.ShowMessage -> {
                    val text = when (event.type) {
                        QuizMessage.QuestionLoadFailed -> getString(QuizRes.MsgQuestionLoadFailed)
                        QuizMessage.AnswerSubmitFailed -> getString(QuizRes.MsgAnswerSubmitFailed)
                    }
                    snackbarHostState.showSnackbar(text)
                }
            }
        }
    }
}