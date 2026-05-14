package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.repository.ChallengeModeRepository

class SetLastChallengeModeUseCase(
    private val repository: ChallengeModeRepository,
) {
    suspend operator fun invoke(mode: ChallengeMode) = repository.setMode(mode)
}