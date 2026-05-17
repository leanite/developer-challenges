package com.leanite.dynaquiz.core.data.repository

import com.leanite.dynaquiz.core.domain.repository.DatabaseRepository
import com.leanite.dynaquiz.database.DynaquizDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DatabaseRepositoryImpl(
    private val databaseProvider: () -> DynaquizDatabase,
    private val ioDispatcher: CoroutineDispatcher,
) : DatabaseRepository {
    override suspend fun warmup() {
        withContext(ioDispatcher) {
            runCatching { databaseProvider() }
        }
    }
}
