package com.tinacyberdeck.outfit.ui.preview

import com.tinacyberdeck.outfit.domain.Category
import com.tinacyberdeck.outfit.domain.Fit
import com.tinacyberdeck.outfit.domain.Garment
import com.tinacyberdeck.outfit.domain.Outfit
import com.tinacyberdeck.outfit.domain.Season
import com.tinacyberdeck.outfit.domain.Style

/**
 * Datos de ejemplo para las @Preview de Compose. Las rutas de imagen van
 * vacías a propósito: en preview no se cargan fotos, solo interesa el layout.
 */
val sampleGarments: List<Garment> = listOf(
    Garment(1, "", Category.TOP, "Camiseta", "#C1440E", Fit.REGULAR, Season.VERANO, Style.CASUAL, 1),
    Garment(2, "", Category.BOTTOM, "Vaquero", "#3A5BA0", Fit.REGULAR, Season.TODO_EL_AÑO, Style.CASUAL, 2),
    Garment(3, "", Category.FOOTWEAR, "Zapatilla", "#4A3220", Fit.REGULAR, Season.TODO_EL_AÑO, Style.DEPORTE, 3),
    Garment(4, "", Category.OUTERWEAR, "Chaqueta", "#4A7C3C", Fit.HOLGADO, Season.ENTRETIEMPO, Style.CASUAL, 4),
    Garment(5, "", Category.ACCESSORY, "Gorro", "#E8930C", Fit.REGULAR, Season.INVIERNO, Style.CASUAL, 5),
    Garment(6, "", Category.BAG, "Mochila", "#6B4423", Fit.REGULAR, Season.TODO_EL_AÑO, Style.CASUAL, 6),
    Garment(7, "", Category.TOP, "Camisa", "#FCE8C2", Fit.CEÑIDO, Season.TODO_EL_AÑO, Style.FORMAL, 7),
    Garment(8, "", Category.BOTTOM, "Pantalón de vestir", "#2A2A2A", Fit.REGULAR, Season.TODO_EL_AÑO, Style.FORMAL, 8),
)

val sampleOutfit: Outfit = Outfit(
    id = 1,
    name = "Look Camiseta + Vaquero",
    top = sampleGarments[0],
    bottom = sampleGarments[1],
    footwear = sampleGarments[2],
    outerwear = sampleGarments[3],
    isFavorite = true,
    createdAt = 1,
)
