package com.leanite.dynaquiz.core.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AnswerRequestDTO(
    val answer: String,
)

@Serializable
internal data class AnswerResultDTO(
    val result: Boolean,
)
