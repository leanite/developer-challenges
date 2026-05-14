package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.model.AnswerResult
import com.leanite.dynaquiz.core.domain.model.QuestionId
import com.leanite.dynaquiz.core.domain.repository.QuizRepository
import com.leanite.dynaquiz.core.domain.result.AppResult

class SubmitAnswerUseCase(
    private val repository: QuizRepository,
) {
    suspend operator fun invoke(
        questionId: QuestionId,
        answer: String,
    ): AppResult<AnswerResult> = repository.submitAnswer(questionId, answer)
}