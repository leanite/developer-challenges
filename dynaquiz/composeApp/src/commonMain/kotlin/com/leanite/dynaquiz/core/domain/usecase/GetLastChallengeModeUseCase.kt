package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.repository.ChallengeModeRepository
import kotlinx.coroutines.flow.Flow

class GetLastChallengeModeUseCase(
    private val repository: ChallengeModeRepository,
) {
    operator fun invoke(): Flow<ChallengeMode> = repository.getMode()
}
