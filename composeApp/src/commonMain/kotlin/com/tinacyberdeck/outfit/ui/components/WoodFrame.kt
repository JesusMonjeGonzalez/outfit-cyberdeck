package com.tinacyberdeck.outfit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tinacyberdeck.outfit.ui.theme.PixelColors

/**
 * Marco de madera de doble borde, como el cuadro de diálogo de
 * creación de personaje de Stardew Valley: borde exterior oscuro,
 * borde interior claro, relleno de pergamino.
 */
@Composable
fun WoodFrame(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(PixelColors.WoodDark)
            .padding(6.dp)
            .background(PixelColors.WoodLight)
            .padding(3.dp)
            .background(PixelColors.WoodMid)
            .padding(10.dp)
            .background(PixelColors.Parchment)
            .border(2.dp, PixelColors.WoodDark)
            .padding(16.dp),
    ) {
        content()
    }
}
