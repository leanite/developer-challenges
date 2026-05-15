package com.leanite.dynaquiz.core.domain.usecase

import com.leanite.dynaquiz.core.domain.model.RankingEntry
import com.leanite.dynaquiz.core.domain.repository.RankingRepository
import com.leanite.dynaquiz.core.domain.result.AppResult

class GetRankingUseCase(
    private val repository: RankingRepository,
) {
    suspend operator fun invoke(): AppResult<List<RankingEntry>> =
        repository.getTopRanking()
}