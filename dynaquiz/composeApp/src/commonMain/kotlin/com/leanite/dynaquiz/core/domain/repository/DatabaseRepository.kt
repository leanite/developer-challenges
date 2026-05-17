package com.leanite.dynaquiz.core.domain.repository

interface DatabaseRepository {
    suspend fun warmup()
}
