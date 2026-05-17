package com.leanite.dynaquiz.fixture

import com.leanite.dynaquiz.core.domain.model.AnswerLog
import com.leanite.dynaquiz.core.domain.model.AnswerOutcome
import com.leanite.dynaquiz.core.domain.model.QuestionId

internal object AnswerLogFixtures {
    private fun log(
        chosen: String? = "A",
        timeRemainingSec: Int = 0,
        outcome: AnswerOutcome = AnswerOutcome.Confirmed(correct = true),
    ): AnswerLog =
        AnswerLog(
            questionId = QuestionId("q-1"),
            chosenAnswer = chosen,
            timeRemainingSec = timeRemainingSec,
            outcome = outcome,
        )

    val confirmedCorrectNoTimeBonus: AnswerLog = log()

    fun confirmedCorrectWithTimeRemaining(seconds: Int): AnswerLog = log(timeRemainingSec = seconds)

    val confirmedIncorrect: AnswerLog =
        log(outcome = AnswerOutcome.Confirmed(correct = false))

    val timedOut: AnswerLog =
        log(chosen = null, outcome = AnswerOutcome.TimedOut)

    val submitFailed: AnswerLog =
        log(outcome = AnswerOutcome.SubmitFailed)
}
