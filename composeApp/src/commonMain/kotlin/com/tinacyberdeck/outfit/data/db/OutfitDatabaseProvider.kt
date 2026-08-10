package com.tinacyberdeck.outfit.data.db

import com.tinacyberdeck.outfit.db.OutfitDatabase

/**
 * Contenedor simple de la instancia única de la base de datos.
 * Cada plataforma llama a [initialize] una vez, muy pronto en su
 * arranque (Application.onCreate en Android, main() en Desktop),
 * pasando el [DatabaseDriverFactory] apropiado.
 */
object OutfitDatabaseProvider {
    private var _database: OutfitDatabase? = null

    val database: OutfitDatabase
        get() = _database ?: error("OutfitDatabaseProvider.initialize() no fue llamado todavía")

    fun initialize(driverFactory: DatabaseDriverFactory) {
        if (_database == null) {
            _database = OutfitDatabase(driverFactory.createDriver())
        }
    }
}
