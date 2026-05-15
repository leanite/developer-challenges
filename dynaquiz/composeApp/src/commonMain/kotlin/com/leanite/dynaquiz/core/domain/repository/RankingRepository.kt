package com.leanite.dynaquiz.core.domain.repository

import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.core.domain.result.AppResult

interface RankingRepository {
    suspend fun saveSession(result: QuizSessionResult): AppResult<Unit>
    suspend fun getTopRanking(): AppResult<List<RankingEntry>>
    suspend fun getTopRankingByPlayerName(playerName: String): AppResult<List<RankingEntry>>
}