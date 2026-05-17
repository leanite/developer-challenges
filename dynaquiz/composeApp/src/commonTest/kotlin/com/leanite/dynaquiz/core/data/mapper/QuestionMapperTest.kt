package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.data.model.QuestionDTO
import com.leanite.dynaquiz.core.domain.model.QuestionId
import kotlin.test.Test
import kotlin.test.assertEquals

class QuestionMapperTest {

    @Test
    fun `toDomain should preserve id wrapping it in QuestionId`() {
        val dto = QuestionDTO(id = "q-42", statement = "Hi?", options = emptyList())

        assertEquals(QuestionId("q-42"), dto.toDomain().id)
    }

    @Test
    fun `toDomain should preserve statement and options as is`() {
        val dto = QuestionDTO(
            id = "q-1",
            statement = "Qual a capital?",
            options = listOf("A", "B", "C", "D"),
        )

        val domain = dto.toDomain()

        assertEquals("Qual a capital?", domain.statement)
        assertEquals(listOf("A", "B", "C", "D"), domain.options)
    }
}
