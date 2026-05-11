package com.leanite.dynaquiz.core.data.repository

import com.leanite.dynaquiz.core.data.datasource.PlayerLocalDataSource
import com.leanite.dynaquiz.core.data.error.toAppError
import com.leanite.dynaquiz.core.data.mapper.toDomain
import com.leanite.dynaquiz.core.domain.model.Player
import com.leanite.dynaquiz.core.domain.repository.PlayerRepository
import com.leanite.dynaquiz.core.domain.result.AppResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.time.Clock

internal class PlayerRepositoryImpl(
    private val localDataSource: PlayerLocalDataSource,
    private val clock: Clock,
    private val ioDispatcher: CoroutineDispatcher,
) : PlayerRepository {
    override suspend fun registerOrFetch(name: String): AppResult<Player> = withContext(ioDispatcher) {
        try {
            val player = localDataSource
                .findOrInsert(name = name, createdAt = clock.now().toEpochMilliseconds())
                .toDomain()
            AppResult.Success(player)
        } catch (throwable: Throwable) {
            AppResult.Error(throwable.toAppError())
        }
    }
}