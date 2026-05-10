package com.leanite.dynaquiz.core.domain

import kotlin.jvm.JvmInline

data class Question(
    val id: QuestionId,
    val statement: String,
    val options: List<String>,
)

@JvmInline
value class QuestionId(val value: String)