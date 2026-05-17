package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.model.Player
import com.leanite.dynaquiz.core.domain.repository.PlayerRepository
import com.leanite.dynaquiz.core.domain.result.AppResult

class RegisterOrFetchPlayerUseCase(
    private val repository: PlayerRepository,
) {
    suspend operator fun invoke(name: String): AppResult<Player> = repository.registerOrFetch(name)
}
