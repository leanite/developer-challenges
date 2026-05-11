package com.leanite.dynaquiz.core.data.error

import com.leanite.dynaquiz.core.domain.result.AppError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

internal fun Throwable.toAppError(): AppError = when (this) {
    is HttpRequestTimeoutException -> AppError.NoInternet
    is ClientRequestException -> when (response.status.value) {
        401, 403 -> AppError.Unauthorized
        404 -> AppError.NotFound
        in 400..499 -> AppError.InvalidData
        else -> AppError.Unknown
    }
    is ServerResponseException -> AppError.ServerError
    is SerializationException -> AppError.InvalidData
    is IOException -> AppError.NoInternet
    else -> AppError.Unknown
}