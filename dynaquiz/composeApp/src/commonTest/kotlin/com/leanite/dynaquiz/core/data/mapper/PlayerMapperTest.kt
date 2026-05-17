package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.domain.model.PlayerId
import com.leanite.dynaquiz.database.PlayerEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class PlayerMapperTest {
    @Test
    fun `toDomain should wrap id in PlayerId and preserve name`() {
        val entity = PlayerEntity(id = 7L, name = "Leandro", createdAt = 0L)

        val domain = entity.toDomain()

        assertEquals(PlayerId(7L), domain.id)
        assertEquals("Leandro", domain.name)
    }

    @Test
    fun `toDomain should convert createdAt epoch millis to Instant`() {
        val epochMillis = 1_700_000_000_000L
        val entity = PlayerEntity(id = 1L, name = "P", createdAt = epochMillis)

        assertEquals(
            Instant.fromEpochMilliseconds(epochMillis),
            entity.toDomain().createdAt,
        )
    }
}
