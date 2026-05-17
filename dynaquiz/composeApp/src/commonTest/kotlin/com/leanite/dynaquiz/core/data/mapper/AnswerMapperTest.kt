package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.data.model.AnswerResultDTO
import com.leanite.dynaquiz.core.domain.model.Answer
import kotlin.test.Test
import kotlin.test.assertEquals

class AnswerMapperTest {

    @Test
    fun `toDomain should map result true to Answer correct true`() {
        val dto = AnswerResultDTO(result = true)

        assertEquals(Answer(correct = true), dto.toDomain())
    }

    @Test
    fun `toDomain should map result false to Answer correct false`() {
        val dto = AnswerResultDTO(result = false)

        assertEquals(Answer(correct = false), dto.toDomain())
    }
}
