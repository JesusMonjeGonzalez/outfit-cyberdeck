package com.tinacyberdeck.outfit.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.tinacyberdeck.outfit.db.OutfitDatabase
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        // BD en la carpeta de la app (junto a las imágenes copiadas), no en el
        // directorio de trabajo. Solo creamos el esquema si el fichero es nuevo:
        // volver a llamar a Schema.create sobre una BD existente falla con
        // "table ... already exists".
        val dbFile = File(System.getProperty("user.home"), ".outfit-cyberdeck/outfit_cyberdeck.db")
        dbFile.parentFile?.mkdirs()
        val isNew = !dbFile.exists()

        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
        if (isNew) {
            OutfitDatabase.Schema.create(driver)
        }
        return driver
    }
}
