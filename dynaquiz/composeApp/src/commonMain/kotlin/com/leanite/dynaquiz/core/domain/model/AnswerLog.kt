package com.leanite.dynaquiz.core.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AnswerLog(
    val questionId: QuestionId,
    val chosenAnswer: String?, // null em caso de acabar o tempo da pergunta
    val timeRemainingSec: Int, // 0 em tempo acabado e Relaxed
    val outcome: AnswerOutcome,
)

@Immutable
sealed interface AnswerOutcome {
    // Backend confirmou certo ou errado
    data class Confirmed(val correct: Boolean) : AnswerOutcome

    // Tempo estourou, nem enviou ao backend
    data object TimedOut : AnswerOutcome

    // Submit falhou (rede/timeout/etc) -> estado pendente para retry futuro
    data object SubmitFailed : AnswerOutcome
}