package com.leanite.dynaquiz.core.data.datasource

import com.leanite.dynaquiz.database.DynaquizDatabase
import com.leanite.dynaquiz.database.PlayerEntity

internal interface PlayerLocalDataSource {
    fun selectByName(name: String): PlayerEntity?

    fun findOrInsert(
        name: String,
        createdAt: Long,
    ): PlayerEntity
}

internal class PlayerLocalDataSourceImpl(
    private val database: DynaquizDatabase,
) : PlayerLocalDataSource {
    private val queries get() = database.playerQueries // gerado pelo SQLDelight

    override fun selectByName(name: String): PlayerEntity? =
        // TODO: verificar necessidade
        queries.selectByName(name).executeAsOneOrNull()

    override fun findOrInsert(
        name: String,
        createdAt: Long,
    ): PlayerEntity =
        database.transactionWithResult {
            val existing = queries.selectByName(name).executeAsOneOrNull()
            existing ?: run {
                queries.insertPlayer(name = name, createdAt = createdAt)
                queries.selectByName(name).executeAsOneOrNull()
                    ?: error("Player '$name' not found right after insert")
            }
        }
}
