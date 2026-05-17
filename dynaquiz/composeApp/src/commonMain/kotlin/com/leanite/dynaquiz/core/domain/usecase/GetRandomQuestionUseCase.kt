package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.repository.QuizRepository
import com.leanite.dynaquiz.core.domain.result.AppResult

class GetRandomQuestionUseCase(
    private val repository: QuizRepository,
) {
    suspend operator fun invoke(): AppResult<Question> = repository.getRandomQuestion()
}
