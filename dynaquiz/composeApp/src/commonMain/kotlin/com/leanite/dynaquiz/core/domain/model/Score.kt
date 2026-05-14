package com.leanite.dynaquiz.core.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class Score(val points: Int) : Comparable<Score> {
    operator fun plus(other: Score): Score = Score(points + other.points)
    override fun compareTo(other: Score): Int = points.compareTo(other.points)

    companion object {
        val Zero: Score = Score(0)
    }
}