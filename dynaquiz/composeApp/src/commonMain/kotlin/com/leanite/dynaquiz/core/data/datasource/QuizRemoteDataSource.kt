package com.leanite.dynaquiz.core.data.datasource

import com.leanite.dynaquiz.core.data.model.QuestionDTO
import com.leanite.dynaquiz.core.data.network.DynaquizJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

internal interface QuizRemoteDataSource {
    suspend fun fetchRandomQuestion(): QuestionDTO
}

internal class QuizRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : QuizRemoteDataSource {

    override suspend fun fetchRandomQuestion(): QuestionDTO {
        val rawJson = httpClient.get("$baseUrl/question").bodyAsText()
        return DynaquizJson.decodeFromString(rawJson)
    }
}