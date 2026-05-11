package com.leanite.dynaquiz.core.data.database

import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.leanite.dynaquiz.database.DynaquizDatabase

internal actual class DatabaseDriverFactory(
    private val context: Context,
) {
    actual fun create(): SqlDriver =
        AndroidSqliteDriver(
            schema = DynaquizDatabase.Schema,
            context = context,
            name = "dynaquiz.db",
            callback = object : AndroidSqliteDriver.Callback(DynaquizDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    // SQLite não habilita FK constraints por default em Android.
                    db.execSQL("PRAGMA foreign_keys=ON;")
                }
            },
        )
}