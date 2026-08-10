package com.tinacyberdeck.outfit.domain

import kotlin.random.Random

/**
 * Genera combinaciones de outfit a partir del armario disponible.
 * Reglas simples y explicables: mismo estilo o compatible y misma temporada
 * o "todo el año".
 */
class OutfitGenerator(private val random: Random = Random.Default) {

    private val compatibleStyles: Map<Style, Set<Style>> = mapOf(
        Style.CASUAL to setOf(Style.CASUAL, Style.GOING_OUT, Style.TRABAJO),
        Style.FORMAL to setOf(Style.FORMAL, Style.TRABAJO),
        Style.DEPORTE to setOf(Style.DEPORTE),
        Style.GOING_OUT to setOf(Style.GOING_OUT, Style.CASUAL),
        Style.TRABAJO to setOf(Style.TRABAJO, Style.FORMAL, Style.CASUAL),
    )

    fun generate(
        wardrobe: List<Garment>,
        preferredStyle: Style? = null,
        preferredSeason: Season? = null,
    ): Outfit? {
        val filtered = wardrobe.filter { garment ->
            (preferredSeason == null || garment.season == preferredSeason || garment.season == Season.TODO_EL_AÑO) &&
                (preferredStyle == null || garment.style in (compatibleStyles[preferredStyle] ?: setOf(preferredStyle)))
        }

        val top = filtered.filter { it.category == Category.TOP }.randomOrNull(random) ?: return null
        val bottom = filtered
            .filter { it.category == Category.BOTTOM }
            .filter { compatibleStyles[top.style]?.contains(it.style) == true }
            .randomOrNull(random)
            ?: filtered.filter { it.category == Category.BOTTOM }.randomOrNull(random)
            ?: return null

        val footwear = filtered.filter { it.category == Category.FOOTWEAR }.randomOrNull(random)
        val outerwear = filtered
            .filter { it.category == Category.OUTERWEAR && (it.season == bottom.season || it.season == Season.TODO_EL_AÑO) }
            .randomOrNull(random)
        val bag = filtered.filter { it.category == Category.BAG }.randomOrNull(random)
        val accessory = filtered.filter { it.category == Category.ACCESSORY }.randomOrNull(random)

        return Outfit(
            name = "Look ${top.garmentType} + ${bottom.garmentType}",
            top = top,
            bottom = bottom,
            footwear = footwear,
            outerwear = outerwear,
            accessory = accessory,
            bag = bag,
            createdAt = currentTimeMillis(),
        )
    }

    private fun <T> List<T>.randomOrNull(random: Random): T? =
        if (isEmpty()) null else this[random.nextInt(size)]
}

expect fun currentTimeMillis(): Long
