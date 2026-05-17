package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.repository.DatabaseRepository

class WarmupDatabaseUseCase(
    private val repository: DatabaseRepository,
) {
    suspend operator fun invoke() = repository.warmup()
}
