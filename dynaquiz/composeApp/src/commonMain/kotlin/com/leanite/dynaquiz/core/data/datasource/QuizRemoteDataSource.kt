package com.leanite.dynaquiz.core.data.datasource

import com.leanite.dynaquiz.core.data.model.QuestionDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

internal interface QuizRemoteDataSource {
    suspend fun fetchRandomQuestion(): QuestionDTO
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
}