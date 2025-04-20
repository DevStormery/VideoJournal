package dev.stormery.photoassignment.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import dev.stormery.photoassignment.database.JournalDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class DatabaseDriverFactory(private val context: Context) {
    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = JournalDatabase.Schema,
            context = context,
            name = "JournalDatabase.db"
        )
    }
}