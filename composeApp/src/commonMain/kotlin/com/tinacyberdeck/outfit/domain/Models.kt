package com.tinacyberdeck.outfit.domain

data class Garment(
    val id: Long = 0,
    val imagePath: String,
    val category: Category,
    val garmentType: String,
    val primaryColor: String, // hex
    val fit: Fit,
    val season: Season,
    val style: Style,
    val createdAt: Long,
)

data class Outfit(
    val id: Long = 0,
    val name: String,
    val top: Garment? = null,
    val bottom: Garment? = null,
    val footwear: Garment? = null,
    val outerwear: Garment? = null,
    val accessory: Garment? = null,
    val bag: Garment? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long,
) {
    /** Todas las prendas no nulas del outfit, en orden de "vestido". */
    fun pieces(): List<Garment> = listOfNotNull(outerwear, top, bottom, footwear, bag, accessory)
}

/** Estado editable en construcción de una nueva prenda, antes de persistir. */
data class DraftGarment(
    val imagePath: String,
    val category: Category = Category.TOP,
    val garmentType: String = GarmentTypes.forCategory(Category.TOP).first(),
    val primaryColor: String = "#8B5A2B",
    val fit: Fit = Fit.REGULAR,
    val season: Season = Season.TODO_EL_AÑO,
    val style: Style = Style.CASUAL,
)
