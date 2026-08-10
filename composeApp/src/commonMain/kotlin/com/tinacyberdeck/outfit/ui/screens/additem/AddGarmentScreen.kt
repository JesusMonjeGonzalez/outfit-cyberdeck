package com.tinacyberdeck.outfit.ui.screens.additem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tinacyberdeck.outfit.ui.components.AttributeRow
import com.tinacyberdeck.outfit.ui.components.PixelButton
import com.tinacyberdeck.outfit.ui.components.RainbowColorSlider
import com.tinacyberdeck.outfit.ui.components.WoodFrame
import com.tinacyberdeck.outfit.ui.theme.PixelColors

/**
 * Pantalla de alta de prenda, calcada del selector de personaje de Stardew
 * Valley: retrato grande de la prenda y panel de atributos con filas
 * ◄ Etiqueta Valor ▶ y slider arcoíris para el color.
 *
 * Es responsiva: en pantallas anchas (desktop, tablet horizontal) coloca el
 * retrato y el panel en fila; en móviles verticales los apila en columna para
 * que nada se salga del borde. Todo el contenido va dentro de un scroll
 * vertical para respetar el texto grande de accesibilidad.
 */
@Composable
fun AddGarmentScreen(
    imagePath: String,
    onConfirm: (AddGarmentState) -> Unit,
    onCancel: () -> Unit,
) {
    val state = remember(imagePath) { AddGarmentState(imagePath) }

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
            Column {
                BoxWithConstraints {
                    val wide = maxWidth >= 520.dp
                    if (wide) {
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            Portrait(imagePath = imagePath, state = state, modifier = Modifier.width(200.dp))
                            Column(
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.weight(1f),
                            ) { AttributeControls(state) }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                        ) {
                            Portrait(imagePath = imagePath, state = state, modifier = Modifier.width(200.dp))
                            Column(
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.fillMaxWidth(),
                            ) { AttributeControls(state) }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    PixelButton(
                        text = "Cancelar",
                        onClick = onCancel,
                        background = PixelColors.WoodMid,
                        modifier = Modifier.weight(1f),
                    )
                    PixelButton(
                        text = "OK",
                        onClick = { onConfirm(state) },
                        background = PixelColors.ConfirmGreen,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

/** Retrato de la prenda con el marco anidado y el nombre debajo. */
@Composable
private fun Portrait(imagePath: String, state: AddGarmentState, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .background(PixelColors.WoodDark)
                .padding(6.dp)
                .background(PixelColors.ParchmentLight),
        ) {
            AsyncImage(
                model = imagePath,
                contentDescription = "Foto de la prenda",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = "${state.category.emoji} ${state.garmentType}",
            color = PixelColors.InkBrown,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
        )
    }
}

/** Filas de atributo + slider de color. Reutilizado en ambos layouts. */
@Composable
private fun ColumnScope.AttributeControls(state: AddGarmentState) {
    Text(
        text = "Nueva prenda",
        color = PixelColors.LabelRed,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
    )
    AttributeRow(
        label = "Categoría",
        value = "${state.category.emoji} ${state.category.label}",
        onPrevious = { state.cycleCategory(false) },
        onNext = { state.cycleCategory(true) },
    )
    AttributeRow(
        label = "Tipo",
        value = state.garmentType,
        onPrevious = { state.cycleGarmentType(false) },
        onNext = { state.cycleGarmentType(true) },
    )
    AttributeRow(
        label = "Ajuste",
        value = state.fit.label,
        onPrevious = { state.cycleFit(false) },
        onNext = { state.cycleFit(true) },
    )
    AttributeRow(
        label = "Temporada",
        value = "${state.season.emoji} ${state.season.label}",
        onPrevious = { state.cycleSeason(false) },
        onNext = { state.cycleSeason(true) },
    )
    AttributeRow(
        label = "Estilo",
        value = state.style.label,
        onPrevious = { state.cycleStyle(false) },
        onNext = { state.cycleStyle(true) },
    )
    Column {
        Text(
            text = "Color",
            color = PixelColors.LabelRed,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(6.dp))
        RainbowColorSlider(
            value = state.colorHue,
            onValueChange = { state.updateColorHue(it) },
        )
    }
}
