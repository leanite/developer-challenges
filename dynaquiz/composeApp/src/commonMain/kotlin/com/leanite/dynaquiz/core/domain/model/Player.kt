package com.leanite.dynaquiz.core.domain.model

import kotlin.time.Instant
import kotlin.jvm.JvmInline

data class Player(
    val id: PlayerId,
    val name: String,
    val createdAt: Instant,
)

@JvmInline
value class PlayerId(val value: Long)