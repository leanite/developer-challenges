package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.domain.model.PlayerId
import com.leanite.dynaquiz.database.PlayerEntity
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerMapperTest {
    @Test
    fun `toDomain should wrap id in PlayerId and preserve name`() {
        val entity = PlayerEntity(id = 7L, name = "Leandro", createdAt = 0L)

        val domain = entity.toDomain()

        assertEquals(PlayerId(7L), domain.id)
        assertEquals("Leandro", domain.name)
    }
}
