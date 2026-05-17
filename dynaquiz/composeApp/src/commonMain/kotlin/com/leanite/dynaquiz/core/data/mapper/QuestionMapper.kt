package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.data.model.QuestionDTO
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuestionId

internal fun QuestionDTO.toDomain(): Question =
    Question(
        id = QuestionId(id),
        statement = statement,
        options = options,
    )
