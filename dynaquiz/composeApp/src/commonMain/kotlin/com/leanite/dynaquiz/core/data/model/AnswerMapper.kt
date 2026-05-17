package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.data.model.AnswerResultDTO
import com.leanite.dynaquiz.core.domain.model.Answer

internal fun AnswerResultDTO.toDomain(): Answer = Answer(correct = result)
