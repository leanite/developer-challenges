package com.leanite.dynaquiz.core.data.repository

import com.leanite.dynaquiz.core.data.datasource.PlayerLocalDataSource
import com.leanite.dynaquiz.core.data.datasource.QuizSessionLocalDataSource
import com.leanite.dynaquiz.core.data.error.toAppError
import com.leanite.dynaquiz.core.data.mapper.toDomain
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.core.domain.repository.RankingRepository
import com.leanite.dynaquiz.core.domain.result.AppResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.time.Clock

internal class RankingRepositoryImpl(
    private val quizSessionDataSource: QuizSessionLocalDataSource,
    private val playerDataSource: PlayerLocalDataSource,
    private val clock: Clock,
    private val ioDispatcher: CoroutineDispatcher,
) : RankingRepository {
    override suspend fun saveSession(result: QuizSessionResult): AppResult<Unit> =
        withContext(ioDispatcher) {
            try {
                val now = clock.now().toEpochMilliseconds()
                // Garante que o player existe e cria como fallback improvável
                val player =
                    playerDataSource.findOrInsert(
                        name = result.playerName,
                        createdAt = now,
                    )
                quizSessionDataSource.insertSession(
                    playerId = player.id,
                    challengeMode = result.challengeMode,
                    scorePoints = result.score.points.toLong(),
                    correctCount = result.correctAnswers.toLong(),
                    totalQuestions = result.totalQuestions.toLong(),
                    finishedAt = now,
                )
                AppResult.Success(Unit)
            } catch (throwable: Throwable) {
                AppResult.Error(throwable.toAppError())
            }
        }

    override suspend fun getTopRanking(): AppResult<List<RankingEntry>> =
        withContext(ioDispatcher) {
            try {
                val entries = quizSessionDataSource.selectRanking().map { it.toDomain() }
                AppResult.Success(entries)
            } catch (throwable: Throwable) {
                AppResult.Error(throwable.toAppError())
            }
        }

    override suspend fun getTopRankingByPlayerName(playerName: String): AppResult<List<RankingEntry>> =
        withContext(ioDispatcher) {
            try {
                val entries =
                    quizSessionDataSource
                        .selectRankingByPlayerName(playerName)
                        .map { it.toDomain() }
                AppResult.Success(entries)
            } catch (throwable: Throwable) {
                AppResult.Error(throwable.toAppError())
            }
        }
}
