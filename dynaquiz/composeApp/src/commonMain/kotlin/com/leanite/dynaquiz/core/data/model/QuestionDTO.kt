package com.leanite.dynaquiz.core.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class QuestionDTO(
    val id: String,
    val statement: String,
    val options: List<String> = emptyList(),
)
