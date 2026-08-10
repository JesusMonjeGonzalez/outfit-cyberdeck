package com.tinacyberdeck.outfit.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.tinacyberdeck.outfit.data.toDomain
import com.tinacyberdeck.outfit.db.OutfitDatabase
import com.tinacyberdeck.outfit.domain.DraftGarment
import com.tinacyberdeck.outfit.domain.Garment
import com.tinacyberdeck.outfit.domain.currentTimeMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WardrobeRepository(
    private val database: OutfitDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private val queries = database.garmentQueries

    fun observeAllGarments(): Flow<List<Garment>> =
        queries.selectAllGarments()
            .asFlow()
            .mapToList(ioDispatcher)
            .map { list -> list.map { it.toDomain() } }

    suspend fun addGarment(draft: DraftGarment): Long = withContext(ioDispatcher) {
        queries.insertGarment(
            imagePath = draft.imagePath,
            category = draft.category.name,
            garmentType = draft.garmentType,
            primaryColor = draft.primaryColor,
            fit = draft.fit.name,
            season = draft.season.name,
            style = draft.style.name,
            createdAt = currentTimeMillis(),
        )
        queries.selectAllGarments().executeAsList().first().id
    }

    suspend fun deleteGarment(id: Long) = withContext(ioDispatcher) {
        queries.deleteGarment(id)
    }

    suspend fun allGarmentsSnapshot(): List<Garment> = withContext(ioDispatcher) {
        queries.selectAllGarments().executeAsList().map { it.toDomain() }
    }
}
