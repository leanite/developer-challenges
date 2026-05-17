package com.leanite.dynaquiz.core.domain.model

import kotlin.jvm.JvmInline
import kotlin.time.Instant

data class Player(
    val id: PlayerId,
    val name: String,
    val createdAt: Instant, // TODO: realmente necessario?
)

@JvmInline
value class PlayerId(
    val value: Long,
)
