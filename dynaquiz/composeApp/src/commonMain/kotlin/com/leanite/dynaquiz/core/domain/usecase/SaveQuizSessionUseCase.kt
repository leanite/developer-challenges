package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.repository.RankingRepository
import com.leanite.dynaquiz.core.domain.result.AppResult

class SaveQuizSessionUseCase(
    private val repository: RankingRepository,
) {
    suspend operator fun invoke(result: QuizSessionResult): AppResult<Unit> =
        repository.saveSession(result)
}