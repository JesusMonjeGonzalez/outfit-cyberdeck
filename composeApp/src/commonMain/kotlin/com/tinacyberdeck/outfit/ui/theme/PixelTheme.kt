package com.tinacyberdeck.outfit.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Una paleta completa. Cada "slot" mantiene su nombre histórico (WoodDark,
 * Parchment...) aunque en el tema Kawaii guarde tonos rosas: son ranuras
 * semánticas por rol (borde, panel, etiqueta, acento...), no colores literales.
 */
data class PixelPalette(
    val name: String,
    val woodDark: Color,
    val woodMid: Color,
    val woodLight: Color,
    val parchment: Color,
    val parchmentLight: Color,
    val labelRed: Color,
    val accentOrange: Color,
    val confirmGreen: Color,
    val sliderTrack: Color,
    val inkBrown: Color,
    val shadow: Color = Color(0x66000000),
)

/** Tema original: cabaña de granja estilo Stardew. */
val FarmPalette = PixelPalette(
    name = "Granja",
    woodDark = Color(0xFF6B4423),
    woodMid = Color(0xFF8B5A2B),
    woodLight = Color(0xFFB07C3E),
    parchment = Color(0xFFF4D9A0),
    parchmentLight = Color(0xFFFCE8C2),
    labelRed = Color(0xFFC1440E),
    accentOrange = Color(0xFFE8930C),
    confirmGreen = Color(0xFF4A7C3C),
    sliderTrack = Color(0xFF3A2A1A),
    inkBrown = Color(0xFF4A3220),
)

/** Tema Kawaii: pastel rosa, dulce y "girly", con verde menta para confirmar. */
val KawaiiPalette = PixelPalette(
    name = "Kawaii",
    woodDark = Color(0xFF7A3B5D),
    woodMid = Color(0xFFC86F9E),
    woodLight = Color(0xFFE7A6C9),
    parchment = Color(0xFFFFE1EF),
    parchmentLight = Color(0xFFFFF2F8),
    labelRed = Color(0xFFD42A7C),
    accentOrange = Color(0xFFFF6FAE),
    confirmGreen = Color(0xFF52C0A6),
    sliderTrack = Color(0xFF4A2438),
    inkBrown = Color(0xFF5A2A44),
)

/**
 * Colores activos de la app. Cada propiedad es estado observable, así que
 * cambiar de tema con [apply]/[toggle] recompone toda la UI al instante sin
 * tocar ni una sola pantalla (todas leen `PixelColors.X`).
 */
object PixelColors {
    var WoodDark by mutableStateOf(FarmPalette.woodDark); private set
    var WoodMid by mutableStateOf(FarmPalette.woodMid); private set
    var WoodLight by mutableStateOf(FarmPalette.woodLight); private set
    var Parchment by mutableStateOf(FarmPalette.parchment); private set
    var ParchmentLight by mutableStateOf(FarmPalette.parchmentLight); private set
    var LabelRed by mutableStateOf(FarmPalette.labelRed); private set
    var AccentOrange by mutableStateOf(FarmPalette.accentOrange); private set
    var ConfirmGreen by mutableStateOf(FarmPalette.confirmGreen); private set
    var SliderTrack by mutableStateOf(FarmPalette.sliderTrack); private set
    var InkBrown by mutableStateOf(FarmPalette.inkBrown); private set
    var Shadow by mutableStateOf(FarmPalette.shadow); private set

    var activeThemeName by mutableStateOf(FarmPalette.name); private set

    fun apply(palette: PixelPalette) {
        WoodDark = palette.woodDark
        WoodMid = palette.woodMid
        WoodLight = palette.woodLight
        Parchment = palette.parchment
        ParchmentLight = palette.parchmentLight
        LabelRed = palette.labelRed
        AccentOrange = palette.accentOrange
        ConfirmGreen = palette.confirmGreen
        SliderTrack = palette.sliderTrack
        InkBrown = palette.inkBrown
        Shadow = palette.shadow
        activeThemeName = palette.name
    }

    /** Alterna entre Granja y Kawaii. */
    fun toggle() = apply(if (activeThemeName == FarmPalette.name) KawaiiPalette else FarmPalette)
}

object PixelDimens {
    val borderThick = 6.dp
    val borderThin = 3.dp
    val cornerRadius = 0.dp // pixel-art: sin bordes redondeados
    val arrowButtonSize = 40.dp
    val panelPadding = 16.dp
}
