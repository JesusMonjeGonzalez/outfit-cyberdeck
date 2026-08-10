package com.tinacyberdeck.outfit.data

import com.tinacyberdeck.outfit.db.GarmentEntity
import com.tinacyberdeck.outfit.db.OutfitEntity
import com.tinacyberdeck.outfit.domain.Category
import com.tinacyberdeck.outfit.domain.Fit
import com.tinacyberdeck.outfit.domain.Garment
import com.tinacyberdeck.outfit.domain.Outfit
import com.tinacyberdeck.outfit.domain.Season
import com.tinacyberdeck.outfit.domain.Style

/** Convierte la fila persistida de una prenda en su modelo de dominio. */
internal fun GarmentEntity.toDomain(): Garment = Garment(
    id = id,
    imagePath = imagePath,
    category = Category.valueOf(category),
    garmentType = garmentType,
    primaryColor = primaryColor,
    fit = Fit.valueOf(fit),
    season = Season.valueOf(season),
    style = Style.valueOf(style),
    createdAt = createdAt,
)

/**
 * Reconstruye un [Outfit] de dominio resolviendo cada id de prenda contra el
 * mapa de prendas actuales del armario. Las prendas borradas quedan como `null`
 * (la FK usa ON DELETE SET NULL), así que un outfit guardado sobrevive aunque
 * alguna de sus piezas ya no exista.
 */
internal fun OutfitEntity.toDomain(garmentsById: Map<Long, Garment>): Outfit = Outfit(
    id = id,
    name = name,
    top = topId?.let { garmentsById[it] },
    bottom = bottomId?.let { garmentsById[it] },
    footwear = footwearId?.let { garmentsById[it] },
    outerwear = outerwearId?.let { garmentsById[it] },
    accessory = accessoryId?.let { garmentsById[it] },
    bag = bagId?.let { garmentsById[it] },
    isFavorite = isFavorite == 1L,
    createdAt = createdAt,
)
