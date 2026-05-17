package com.leanite.dynaquiz.core.domain.repository

import com.leanite.dynaquiz.core.domain.model.Player
import com.leanite.dynaquiz.core.domain.result.AppResult

interface PlayerRepository {
    suspend fun registerOrFetch(name: String): AppResult<Player>
}
