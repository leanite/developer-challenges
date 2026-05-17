package com.leanite.dynaquiz.core.domain.repository

interface UserRepository {
    suspend fun getLastNickname(): String?

    suspend fun setLastNickname(nickname: String)
}
