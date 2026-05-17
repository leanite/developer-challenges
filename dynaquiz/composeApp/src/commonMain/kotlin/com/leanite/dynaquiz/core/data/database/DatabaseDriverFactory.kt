package com.leanite.dynaquiz.core.data.database

import app.cash.sqldelight.db.SqlDriver

internal expect class DatabaseDriverFactory {
    fun create(): SqlDriver
}
