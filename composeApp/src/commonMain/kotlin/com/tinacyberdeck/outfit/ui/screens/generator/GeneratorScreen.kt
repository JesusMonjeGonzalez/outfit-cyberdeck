package com.tinacyberdeck.outfit.ui.screens.generator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tinacyberdeck.outfit.domain.Garment
import com.tinacyberdeck.outfit.domain.Outfit
import com.tinacyberdeck.outfit.domain.Season
import com.tinacyberdeck.outfit.domain.Style
import com.tinacyberdeck.outfit.ui.components.AttributeRow
import com.tinacyberdeck.outfit.ui.components.MascotHelper
import com.tinacyberdeck.outfit.ui.components.PixelButton
import com.tinacyberdeck.outfit.ui.components.WoodFrame
import com.tinacyberdeck.outfit.ui.theme.PixelColors
import kotlinx.coroutines.delay

/**
 * Pantalla del generador de outfits. Deja elegir un [Style] y una [Season]
 * preferidos (o "Cualquiera"), y revela el look resultante pieza a pieza con
 * una animación en cascada.
 */
@Composable
fun GeneratorScreen(
    outfit: Outfit?,
    onGenerateClick: (preferredStyle: Style?, preferredSeason: Season?) -> Unit,
    onSaveClick: () -> Unit,
) {
    val styleOptions = remember { listOf<Style?>(null) + Style.entries }
    val seasonOptions = remember { listOf<Season?>(null) + Season.entries }
    var preferredStyle by remember { mutableStateOf<Style?>(null) }
    var preferredSeason by remember { mutableStateOf<Season?>(null) }
    // Cambia con cada tirada para reiniciar la animación en cascada.
    var generation by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PixelColors.WoodDark)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        WoodFrame(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp)
                .padding(horizontal = 16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Generador de outfits",
                    color = PixelColors.LabelRed,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                )
                Spacer(Modifier.height(12.dp))

                MascotHelper(
                    message = if (outfit == null) {
                        "Elige tus gustos y ¡tira el dado! 🎲"
                    } else {
                        "¡Buen look! Dale a ★ para guardarlo."
                    },
                    compact = true,
                )

                Spacer(Modifier.height(14.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    AttributeRow(
                        label = "Estilo",
                        value = preferredStyle?.label ?: "Cualquiera",
                        onPrevious = { preferredStyle = cyclePref(styleOptions, preferredStyle, forward = false) },
                        onNext = { preferredStyle = cyclePref(styleOptions, preferredStyle, forward = true) },
                    )
                    AttributeRow(
                        label = "Temporada",
                        value = preferredSeason?.let { "${it.emoji} ${it.label}" } ?: "Cualquiera",
                        onPrevious = { preferredSeason = cyclePref(seasonOptions, preferredSeason, forward = false) },
                        onNext = { preferredSeason = cyclePref(seasonOptions, preferredSeason, forward = true) },
                    )
                }

                Spacer(Modifier.height(16.dp))

                if (outfit == null) {
                    Text(
                        text = "Aún no hay look. Pulsa 🎲 Generar\npara crear uno con tu armario.",
                        color = PixelColors.InkBrown,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                } else {
                    Text(
                        text = outfit.name,
                        color = PixelColors.InkBrown,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                    Spacer(Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        itemsIndexed(outfit.pieces(), key = { _, g -> g.id }) { index, garment ->
                            AnimatedPiece(generation = generation, index = index) {
                                PieceThumbnail(garment)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    PixelButton(
                        text = "🎲 Generar",
                        background = PixelColors.AccentOrange,
                        onClick = {
                            generation++
                            onGenerateClick(preferredStyle, preferredSeason)
                        },
                        modifier = Modifier.weight(1f),
                    )
                    if (outfit != null) {
                        PixelButton(
                            text = "★ Guardar",
                            background = PixelColors.ConfirmGreen,
                            onClick = onSaveClick,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

/** Aparición en cascada de una prenda: fade + deslizamiento + escala, escalonada por índice. */
@Composable
private fun AnimatedPiece(generation: Int, index: Int, content: @Composable () -> Unit) {
    var shown by remember(generation, index) { mutableStateOf(false) }
    LaunchedEffect(generation, index) {
        delay(60L + index * 90L)
        shown = true
    }
    AnimatedVisibility(
        visible = shown,
        enter = fadeIn(tween(240)) +
            slideInVertically(tween(240)) { it / 3 } +
            scaleIn(tween(240), initialScale = 0.85f),
    ) {
        content()
    }
}

@Composable
private fun PieceThumbnail(garment: Garment) {
    Box(
        modifier = Modifier
            .height(140.dp)
            .aspectRatio(0.8f)
            .background(PixelColors.WoodDark)
            .padding(4.dp)
            .background(PixelColors.ParchmentLight),
    ) {
        AsyncImage(
            model = garment.imagePath,
            contentDescription = garment.garmentType,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Text(
            text = garment.category.emoji,
            fontSize = 13.sp,
            modifier = Modifier.align(Alignment.TopEnd).padding(2.dp),
        )
    }
}

/** Cicla por una lista de opciones que incluye `null` ("Cualquiera") al inicio. */
private fun <T> cyclePref(options: List<T>, current: T, forward: Boolean): T {
    val idx = options.indexOf(current).let { if (it == -1) 0 else it }
    val next = if (forward) (idx + 1) % options.size else (idx - 1 + options.size) % options.size
    return options[next]
}
