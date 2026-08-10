package com.tinacyberdeck.outfit.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Convierte un color hex "#RRGGBB" (el formato que guardamos en la prenda) a un
 * [Color] de Compose, de forma multiplataforma. Si el texto no es válido,
 * devuelve [fallback] en lugar de reventar.
 */
fun parseHexColor(hex: String, fallback: Color = PixelColors.WoodMid): Color {
    val clean = hex.removePrefix("#")
    if (clean.length != 6) return fallback
    return runCatching {
        Color(
            red = clean.substring(0, 2).toInt(16),
            green = clean.substring(2, 4).toInt(16),
            blue = clean.substring(4, 6).toInt(16),
        )
    }.getOrDefault(fallback)
}

/** Oscurece un color multiplicando sus canales — para el estado "pulsado" pixel. */
fun Color.darker(factor: Float = 0.82f): Color =
    Color(red * factor, green * factor, blue * factor, alpha)

/**
 * Color de acento por categoría, para dar un toque más vivo (chips, etiquetas)
 * sin salirnos del universo cálido de la paleta.
 */
fun com.tinacyberdeck.outfit.domain.Category.accent(): Color = when (this) {
    com.tinacyberdeck.outfit.domain.Category.TOP -> PixelColors.LabelRed
    com.tinacyberdeck.outfit.domain.Category.BOTTOM -> Color(0xFF3A6EA5)
    com.tinacyberdeck.outfit.domain.Category.FOOTWEAR -> PixelColors.InkBrown
    com.tinacyberdeck.outfit.domain.Category.OUTERWEAR -> PixelColors.ConfirmGreen
    com.tinacyberdeck.outfit.domain.Category.ACCESSORY -> PixelColors.AccentOrange
    com.tinacyberdeck.outfit.domain.Category.BAG -> Color(0xFF8A5A9E)
}
