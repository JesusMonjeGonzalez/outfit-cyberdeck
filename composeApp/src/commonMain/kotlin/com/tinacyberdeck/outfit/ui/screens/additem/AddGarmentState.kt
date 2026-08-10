package com.tinacyberdeck.outfit.ui.screens.additem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tinacyberdeck.outfit.domain.Category
import com.tinacyberdeck.outfit.domain.DraftGarment
import com.tinacyberdeck.outfit.domain.Fit
import com.tinacyberdeck.outfit.domain.GarmentTypes
import com.tinacyberdeck.outfit.domain.Season
import com.tinacyberdeck.outfit.domain.Style
import com.tinacyberdeck.outfit.ui.components.hueToHex

/**
 * Mantiene el DraftGarment en construcción y expone las funciones de
 * ciclo para cada atributo, tal como los botones ◄ ▶ de Stardew Valley
 * recorren Skin, Hair, Shirt, Pants...
 */
class AddGarmentState(imagePath: String) {
    var category by mutableStateOf(Category.TOP)
        private set
    var garmentType by mutableStateOf(GarmentTypes.forCategory(Category.TOP).first())
        private set
    var fit by mutableStateOf(Fit.REGULAR)
        private set
    var season by mutableStateOf(Season.TODO_EL_AÑO)
        private set
    var style by mutableStateOf(Style.CASUAL)
        private set
    var colorHue by mutableStateOf(0.08f) // arranca en un tono marrón/madera
        private set

    val imagePath: String = imagePath

    fun cycleCategory(forward: Boolean) {
        category = Category.cycle(category, forward)
        garmentType = GarmentTypes.forCategory(category).first()
    }

    fun cycleGarmentType(forward: Boolean) {
        garmentType = GarmentTypes.cycle(category, garmentType, forward)
    }

    fun cycleFit(forward: Boolean) {
        fit = Fit.cycle(fit, forward)
    }

    fun cycleSeason(forward: Boolean) {
        season = Season.cycle(season, forward)
    }

    fun cycleStyle(forward: Boolean) {
        style = Style.cycle(style, forward)
    }

    fun updateColorHue(hue: Float) {
        colorHue = hue.coerceIn(0f, 1f)
    }

    fun toDraft(): DraftGarment = DraftGarment(
        imagePath = imagePath,
        category = category,
        garmentType = garmentType,
        primaryColor = hueToHex(colorHue),
        fit = fit,
        season = season,
        style = style,
    )
}
