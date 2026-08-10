package com.tinacyberdeck.outfit.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinacyberdeck.outfit.ui.theme.PixelColors

enum class ArrowDirection { LEFT, RIGHT, UP, DOWN }

/**
 * Botón flecha (naranja, punta clara) que evoca el selector de atributos de
 * Stardew Valley. El estado "pulsado" se deriva del [MutableInteractionSource]
 * (antes se quedaba rojo para siempre tras el primer toque) y añade un pequeño
 * encogido como feedback.
 */
@Composable
fun PixelArrowButton(
    direction: ArrowDirection,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.85f else 1f, label = "arrowScale")

    val glyph = when (direction) {
        ArrowDirection.LEFT -> "◀"
        ArrowDirection.RIGHT -> "▶"
        ArrowDirection.UP -> "▲"
        ArrowDirection.DOWN -> "▼"
    }
    val bg = when {
        !enabled -> Color(0xFFBFA98A)
        pressed -> PixelColors.LabelRed
        else -> PixelColors.AccentOrange
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(36.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(CutCornerShape(6.dp))
            .background(bg)
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
    ) {
        Text(text = glyph, color = PixelColors.ParchmentLight, fontSize = 16.sp)
    }
}
