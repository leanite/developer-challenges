package com.leanite.dynaquiz.core.data.datasource

import com.leanite.dynaquiz.core.data.model.AnswerRequestDTO
import com.leanite.dynaquiz.core.data.model.AnswerResultDTO
import com.leanite.dynaquiz.core.data.model.QuestionDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

internal interface QuizRemoteDataSource {
    suspend fun fetchRandomQuestion(): QuestionDTO
    suspend fun submitAnswer(questionId: String, answer: String): AnswerResultDTO
}

internal class QuizRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val jsonConfig: Json,
) : QuizRemoteDataSource {

    override suspend fun fetchRandomQuestion(): QuestionDTO {
        val rawJson = httpClient.get("$baseUrl/question").bodyAsText()
        return jsonConfig.decodeFromString(rawJson)
    }

    override suspend fun submitAnswer(questionId: String, answer: String): AnswerResultDTO {
        val rawJson = httpClient.post("$baseUrl/answer") {
            parameter("questionId", questionId)
            contentType(ContentType.Application.Json)
            setBody(AnswerRequestDTO(answer))
        }.bodyAsText()
        return jsonConfig.decodeFromString(rawJson)
    }
}