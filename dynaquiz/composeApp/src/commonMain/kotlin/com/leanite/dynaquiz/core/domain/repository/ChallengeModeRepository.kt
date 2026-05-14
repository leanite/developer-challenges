package com.leanite.dynaquiz.core.domain.repository

import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import kotlinx.coroutines.flow.Flow

interface ChallengeModeRepository {
    fun getMode(): Flow<ChallengeMode>
    suspend fun setMode(mode: ChallengeMode)
}