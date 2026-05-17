package com.leanite.dynaquiz.core.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.leanite.dynaquiz.database.DynaquizDatabase

internal actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver =
        NativeSqliteDriver(
            schema = DynaquizDatabase.Schema,
            name = "dynaquiz.db",
        )
}
