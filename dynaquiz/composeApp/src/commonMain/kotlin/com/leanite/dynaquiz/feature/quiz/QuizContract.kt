package com.leanite.dynaquiz.feature.quiz

import androidx.compose.runtime.Immutable
import com.leanite.dynaquiz.core.domain.model.AnswerLog
import com.leanite.dynaquiz.core.domain.model.AnswerOutcome
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult

@Immutable
sealed interface QuizPhase {
    data class Countdown(
        val countdownSecondsRemaining: Int,
    ) : QuizPhase

    data object Loading : QuizPhase

    data class Playing(
        val question: Question,
        val selectedAnswer: String? = null,
        val isSubmitting: Boolean = false,
    ) : QuizPhase

    data object Completed : QuizPhase
}

@Immutable
data class QuizUiState(
    val challengeMode: ChallengeMode,
    val phase: QuizPhase = QuizPhase.Countdown(QuizRules.INITIAL_COUNTDOWN_SECONDS),
    val currentQuestionIndex: Int = 0,
    val answerLog: List<AnswerLog> = emptyList(),
    val timeRemainingSec: Int? = null,
    val showExitDialog: Boolean = false,
) {
    val questionNumber: Int
        get() = currentQuestionIndex + 1

    val correctAnswers: Int
        get() = answerLog.count { (it.outcome as? AnswerOutcome.Confirmed)?.correct == true }
}

@Immutable
sealed interface QuizIntent {
    data object Started : QuizIntent

    data class AnswerSelected(
        val answer: String,
    ) : QuizIntent

    data object BackPressed : QuizIntent

    data object ExitConfirmed : QuizIntent

    data object ExitCancelled : QuizIntent
}

@Immutable
sealed interface QuizEvent {
    data class NavigateToResult(
        val result: QuizSessionResult,
    ) : QuizEvent

    data object NavigateBack : QuizEvent

    data class ShowMessage(
        val type: QuizMessage,
    ) : QuizEvent
}

@Immutable
sealed interface QuizMessage {
    data object QuestionLoadFailed : QuizMessage

    data object AnswerSubmitFailed : QuizMessage
}

internal object QuizRules {
    const val TOTAL_QUESTIONS = 10
    const val INITIAL_COUNTDOWN_SECONDS = 3
}
