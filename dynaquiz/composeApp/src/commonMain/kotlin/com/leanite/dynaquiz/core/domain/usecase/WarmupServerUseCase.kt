package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.repository.QuizRepository

class WarmupServerUseCase(
    private val repository: QuizRepository,
) {
    suspend operator fun invoke() = repository.warmupServer()
}
