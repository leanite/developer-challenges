package com.leanite.dynaquiz.core.data.datasource

import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.database.DynaquizDatabase
import com.leanite.dynaquiz.database.SelectRanking
import com.leanite.dynaquiz.database.SelectRankingByPlayerName

internal interface QuizSessionLocalDataSource {
    fun insertSession(
        playerId: Long,
        challengeMode: ChallengeMode,
        scorePoints: Long,
        correctCount: Long,
        totalQuestions: Long,
        finishedAt: Long,
    )
    fun selectRanking(): List<SelectRanking>
    fun selectRankingByPlayerName(playerName: String): List<SelectRankingByPlayerName>
}

internal class QuizSessionLocalDataSourceImpl(
    private val database: DynaquizDatabase,
) : QuizSessionLocalDataSource {

    private val queries get() = database.quizSessionQueries

    override fun insertSession(
        playerId: Long,
        challengeMode: ChallengeMode,
        scorePoints: Long,
        correctCount: Long,
        totalQuestions: Long,
        finishedAt: Long,
    ) {
        queries.insertSession(
            playerId = playerId,
            challengeMode = challengeMode,
            scorePoints = scorePoints,
            correctCount = correctCount,
            totalQuestions = totalQuestions,
            finishedAt = finishedAt,
        )
    }

    override fun selectRanking(): List<SelectRanking> =
        queries.selectRanking().executeAsList()

    override fun selectRankingByPlayerName(playerName: String): List<SelectRankingByPlayerName> =
        queries.selectRankingByPlayerName(playerName).executeAsList()
}