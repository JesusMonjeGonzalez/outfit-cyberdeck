package com.tinacyberdeck.outfit.data.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.tinacyberdeck.outfit.db.OutfitDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(OutfitDatabase.Schema, context, "outfit_cyberdeck.db")
    }
}
