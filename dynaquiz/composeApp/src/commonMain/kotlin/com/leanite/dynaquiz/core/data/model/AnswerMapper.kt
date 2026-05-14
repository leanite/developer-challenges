package com.leanite.dynaquiz.core.data.mapper

import com.leanite.dynaquiz.core.data.model.AnswerResultDTO
import com.leanite.dynaquiz.core.domain.model.AnswerResult

internal fun AnswerResultDTO.toDomain(): AnswerResult = AnswerResult(correct = result)