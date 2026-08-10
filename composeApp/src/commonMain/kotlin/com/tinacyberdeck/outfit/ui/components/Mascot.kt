package com.tinacyberdeck.outfit.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinacyberdeck.outfit.ui.theme.PixelColors
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * "Aguja", la mascota — dibujada como un **Tamagotchi**: carcasa con forma de
 * huevo (color de acento del tema), anilla de llavero arriba, tres botones, y
 * una pantalla LCD donde vive una criatura pixel que se balancea y parpadea.
 * Todo en Canvas, sin assets, y usando los colores del tema activo (se vuelve
 * rosa en el tema Kawaii automáticamente).
 */
@Composable
fun MascotSprite(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
) {
    val transition = rememberInfiniteTransition(label = "mascot")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * kotlin.math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart),
        label = "bob",
    )

    var blink by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(2600)
            blink = true
            kotlinx.coroutines.delay(130)
            blink = false
        }
    }

    // Lectura de colores dentro de composición para que Canvas se repinte al cambiar de tema.
    val shell = PixelColors.AccentOrange
    val ink = PixelColors.WoodDark
    val screenBg = PixelColors.ParchmentLight
    val pet = PixelColors.InkBrown
    val petEye = PixelColors.ParchmentLight
    val cheek = PixelColors.LabelRed

    // Rebote al tocar a Aguja: un saltito elástico.
    val scope = rememberCoroutineScope()
    val pop = remember { Animatable(1f) }
    val interaction = remember { MutableInteractionSource() }

    Canvas(
        modifier = modifier
            .size(size)
            .graphicsLayer { scaleX = pop.value; scaleY = pop.value }
            .clickable(interactionSource = interaction, indication = null) {
                scope.launch {
                    pop.animateTo(1.22f, spring(stiffness = Spring.StiffnessHigh))
                    pop.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
            },
    ) {
        val w = this.size.width
        val h = this.size.height
        val s = this.size.minDimension
        val cx = w / 2f
        val stroke = s * 0.045f

        // Anilla de llavero.
        drawCircle(ink, radius = s * 0.06f, center = Offset(cx, h * 0.08f), style = Stroke(stroke))

        // Carcasa tipo huevo.
        drawRoundRect(
            color = shell,
            topLeft = Offset(w * 0.1f, h * 0.14f),
            size = Size(w * 0.8f, h * 0.82f),
            cornerRadius = CornerRadius(w * 0.4f, h * 0.34f),
        )
        drawRoundRect(
            color = ink,
            topLeft = Offset(w * 0.1f, h * 0.14f),
            size = Size(w * 0.8f, h * 0.82f),
            cornerRadius = CornerRadius(w * 0.4f, h * 0.34f),
            style = Stroke(stroke),
        )

        // Pantalla LCD.
        val scrLeft = w * 0.24f
        val scrTop = h * 0.26f
        val scrW = w * 0.52f
        val scrH = h * 0.4f
        drawRoundRect(screenBg, Offset(scrLeft, scrTop), Size(scrW, scrH), CornerRadius(s * 0.08f))
        drawRoundRect(ink, Offset(scrLeft, scrTop), Size(scrW, scrH), CornerRadius(s * 0.08f), style = Stroke(s * 0.035f))

        // Criatura pixel dentro de la pantalla.
        val petCx = cx
        val petCy = scrTop + scrH * 0.52f + sin(phase) * s * 0.025f
        drawCircle(pet, radius = s * 0.12f, center = Offset(petCx, petCy))
        // Orejitas.
        drawCircle(pet, radius = s * 0.045f, center = Offset(petCx - s * 0.09f, petCy - s * 0.11f))
        drawCircle(pet, radius = s * 0.045f, center = Offset(petCx + s * 0.09f, petCy - s * 0.11f))
        // Mejillas.
        drawCircle(cheek.copy(alpha = 0.5f), radius = s * 0.035f, center = Offset(petCx - s * 0.075f, petCy + s * 0.02f))
        drawCircle(cheek.copy(alpha = 0.5f), radius = s * 0.035f, center = Offset(petCx + s * 0.075f, petCy + s * 0.02f))
        // Ojos (o guiño).
        val eyeY = petCy - s * 0.02f
        val eyeDx = s * 0.05f
        if (blink) {
            drawLine(petEye, Offset(petCx - eyeDx - s * 0.03f, eyeY), Offset(petCx - eyeDx + s * 0.03f, eyeY), strokeWidth = s * 0.025f)
            drawLine(petEye, Offset(petCx + eyeDx - s * 0.03f, eyeY), Offset(petCx + eyeDx + s * 0.03f, eyeY), strokeWidth = s * 0.025f)
        } else {
            drawCircle(petEye, radius = s * 0.028f, center = Offset(petCx - eyeDx, eyeY))
            drawCircle(petEye, radius = s * 0.028f, center = Offset(petCx + eyeDx, eyeY))
        }

        // Tres botones inferiores.
        val btnY = h * 0.84f
        listOf(-0.16f, 0f, 0.16f).forEach { dx ->
            drawCircle(ink, radius = s * 0.045f, center = Offset(cx + w * dx, btnY))
        }
    }
}

/**
 * Aguja + globo de diálogo con un consejo. [compact] reduce el tamaño para
 * cabeceras.
 */
@Composable
fun MascotHelper(
    message: String,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        MascotSprite(size = if (compact) 46.dp else 68.dp)
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .clip(CutCornerShape(10.dp))
                .background(PixelColors.ParchmentLight)
                .border(2.dp, PixelColors.WoodDark, CutCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(
                text = message,
                color = PixelColors.InkBrown,
                fontWeight = FontWeight.SemiBold,
                fontSize = if (compact) 12.sp else 13.sp,
            )
        }
    }
}
