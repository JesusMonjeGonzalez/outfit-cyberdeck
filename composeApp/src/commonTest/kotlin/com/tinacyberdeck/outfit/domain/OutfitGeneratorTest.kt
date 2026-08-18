package com.tinacyberdeck.outfit.domain

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class OutfitGeneratorTest {
    @Test
    fun generatesRequiredTopAndBottom() {
        val outfit = OutfitGenerator(Random(1)).generate(
            wardrobe = listOf(
                garment(Category.TOP, "Camiseta", Style.CASUAL),
                garment(Category.BOTTOM, "Vaquero", Style.CASUAL),
                garment(Category.FOOTWEAR, "Zapatilla", Style.CASUAL),
            ),
            preferredStyle = Style.CASUAL,
            preferredSeason = Season.VERANO,
        )

        assertNotNull(outfit)
        assertEquals(Category.TOP, outfit.top?.category)
        assertEquals(Category.BOTTOM, outfit.bottom?.category)
        assertEquals(Category.FOOTWEAR, outfit.footwear?.category)
    }

    @Test
    fun allYearGarmentsMatchASeasonFilter() {
        val outfit = OutfitGenerator(Random(2)).generate(
            wardrobe = listOf(
                garment(Category.TOP, "Camiseta", Style.CASUAL, Season.VERANO),
                garment(Category.BOTTOM, "Vaquero", Style.CASUAL, Season.TODO_EL_AÑO),
            ),
            preferredStyle = Style.CASUAL,
            preferredSeason = Season.VERANO,
        )

        assertNotNull(outfit)
        assertEquals("Camiseta", outfit.top?.garmentType)
        assertEquals("Vaquero", outfit.bottom?.garmentType)
    }

    @Test
    fun incompleteWardrobeDoesNotCreateAnOutfit() {
        val outfit = OutfitGenerator(Random(3)).generate(
            wardrobe = listOf(garment(Category.TOP, "Camiseta", Style.CASUAL)),
        )

        assertNull(outfit)
    }

    private fun garment(
        category: Category,
        type: String,
        style: Style,
        season: Season = Season.TODO_EL_AÑO,
    ) = Garment(
        imagePath = "file:///tmp/$type.jpg",
        category = category,
        garmentType = type,
        primaryColor = "#ffffff",
        fit = Fit.REGULAR,
        season = season,
        style = style,
        createdAt = 0,
    )
}
