package com.tinacyberdeck.outfit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.tinacyberdeck.outfit.ui.theme.PixelColors
import kotlin.math.roundToInt

/**
 * Slider horizontal con degradado arcoíris de fondo (tal como los
 * sliders de Skin/Eye/Hair/Pants Color en Stardew) y un knob que se
 * arrastra. Devuelve un valor 0f..1f mapeable a un color HSV.
 */
@Composable
fun RainbowColorSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rainbow = Brush.horizontalGradient(
        colors = listOf(
            Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red,
        ),
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val newValue = (change.position.x / size.width).coerceIn(0f, 1f)
                    onValueChange(newValue)
                }
            },
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(10.dp)) {
            drawRect(brush = rainbow)
            drawRect(color = PixelColors.WoodDark, style = Stroke(width = 2f))
        }
        Canvas(modifier = Modifier.fillMaxWidth().height(20.dp)) {
            val knobX = value * size.width
            drawCircle(
                color = PixelColors.InkBrown,
                radius = 9f,
                center = Offset(knobX, size.height / 2f),
            )
            drawCircle(
                color = Color.White,
                radius = 5f,
                center = Offset(knobX, size.height / 2f),
            )
        }
    }
}

/** Convierte un hue 0f..1f a un color hex "#RRGGBB" a saturación/brillo fijos. */
fun hueToHex(hue: Float): String {
    val color = Color.hsv(hue * 360f, 0.75f, 0.85f)
    val r = (color.red * 255).roundToInt()
    val g = (color.green * 255).roundToInt()
    val b = (color.blue * 255).roundToInt()
    return "#${r.toHexByte()}${g.toHexByte()}${b.toHexByte()}"
}

private fun Int.toHexByte(): String = coerceIn(0, 255).toString(16).padStart(2, '0')
