package com.leanite.dynaquiz.core.data.repository

import com.leanite.dynaquiz.core.data.datasource.ChallengeModeLocalDataSource
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.repository.ChallengeModeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ChallengeModeRepositoryImpl(
    private val dataSource: ChallengeModeLocalDataSource,
) : ChallengeModeRepository {
    override fun getMode(): Flow<ChallengeMode> =
        dataSource.storedMode.map { raw ->
            raw?.let { ChallengeMode.fromSerializedName(it) } ?: DEFAULT_CHALLENGE_MODE
        }

    override suspend fun setMode(mode: ChallengeMode) {
        dataSource.setStoredMode(mode.serializedName)
    }
}

private val DEFAULT_CHALLENGE_MODE: ChallengeMode = ChallengeMode.Timed.Easy
