package com.tinacyberdeck.outfit.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.tinacyberdeck.outfit.data.toDomain
import com.tinacyberdeck.outfit.db.OutfitDatabase
import com.tinacyberdeck.outfit.domain.Outfit
import com.tinacyberdeck.outfit.domain.currentTimeMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

/**
 * Persistencia de outfits guardados. Sigue el mismo patrón que
 * [WardrobeRepository]: expone flujos observables y operaciones suspend.
 */
class OutfitRepository(
    private val database: OutfitDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private val outfitQueries = database.outfitQueries
    private val garmentQueries = database.garmentQueries

    /**
     * Outfits marcados como favoritos, con sus prendas ya resueltas. Combina el
     * flujo de outfits con el de prendas para que, si una prenda cambia o se
     * borra, los favoritos se recalculen solos.
     */
    fun observeFavoriteOutfits(): Flow<List<Outfit>> =
        combine(
            outfitQueries.selectFavoriteOutfits().asFlow().mapToList(ioDispatcher),
            garmentQueries.selectAllGarments().asFlow().mapToList(ioDispatcher),
        ) { outfits, garments ->
            val byId = garments.associate { it.id to it.toDomain() }
            outfits.map { it.toDomain(byId) }
        }

    /** Guarda un outfit generado como favorito. */
    suspend fun saveFavorite(outfit: Outfit): Unit = withContext(ioDispatcher) {
        outfitQueries.insertOutfit(
            name = outfit.name,
            topId = outfit.top?.id,
            bottomId = outfit.bottom?.id,
            footwearId = outfit.footwear?.id,
            outerwearId = outfit.outerwear?.id,
            accessoryId = outfit.accessory?.id,
            bagId = outfit.bag?.id,
            isFavorite = 1L,
            createdAt = currentTimeMillis(),
        )
    }

    suspend fun deleteOutfit(id: Long): Unit = withContext(ioDispatcher) {
        outfitQueries.deleteOutfit(id)
    }
}
