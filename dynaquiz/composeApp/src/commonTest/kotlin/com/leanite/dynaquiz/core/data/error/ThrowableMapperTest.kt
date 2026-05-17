package com.leanite.dynaquiz.core.data.error

import com.leanite.dynaquiz.core.domain.result.AppError
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertEquals

class ThrowableMapperTest {
    @Test
    fun `SerializationException should map to InvalidData`() {
        val throwable: Throwable = SerializationException("bad json")

        assertEquals(AppError.InvalidData, throwable.toAppError())
    }

    @Test
    fun `IOException should map to NoInternet`() {
        val throwable: Throwable = IOException("connection refused")

        assertEquals(AppError.NoInternet, throwable.toAppError())
    }

    @Test
    fun `untyped Throwable should map to Unknown`() {
        val throwable: Throwable = RuntimeException("boom")

        assertEquals(AppError.Unknown, throwable.toAppError())
    }
}
