package com.leanite.dynaquiz.core.domain.model

import kotlin.jvm.JvmInline

data class Player(
    val id: PlayerId,
    val name: String,
)

@JvmInline
value class PlayerId(
    val value: Long,
)
