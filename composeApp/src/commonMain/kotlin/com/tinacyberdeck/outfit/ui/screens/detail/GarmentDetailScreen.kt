package com.tinacyberdeck.outfit.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tinacyberdeck.outfit.domain.Garment
import com.tinacyberdeck.outfit.ui.components.PixelButton
import com.tinacyberdeck.outfit.ui.components.WoodFrame
import com.tinacyberdeck.outfit.ui.theme.PixelColors
import com.tinacyberdeck.outfit.ui.theme.parseHexColor

/**
 * Detalle de solo lectura de una prenda guardada, con el mismo lenguaje visual
 * que la pantalla de alta (retrato + filas de atributo), más un botón para
 * eliminarla del armario.
 */
@Composable
fun GarmentDetailScreen(
    garment: Garment,
    onBack: () -> Unit,
    onDelete: (Garment) -> Unit,
) {
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
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "${garment.category.emoji} ${garment.garmentType}",
                    color = PixelColors.LabelRed,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                )

                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .aspectRatio(0.85f)
                        .align(Alignment.CenterHorizontally)
                        .background(PixelColors.WoodDark)
                        .padding(6.dp)
                        .background(PixelColors.ParchmentLight),
                ) {
                    AsyncImage(
                        model = garment.imagePath,
                        contentDescription = garment.garmentType,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ReadOnlyRow("Categoría", "${garment.category.emoji} ${garment.category.label}")
                    ReadOnlyRow("Tipo", garment.garmentType)
                    ReadOnlyRow("Ajuste", garment.fit.label)
                    ReadOnlyRow("Temporada", "${garment.season.emoji} ${garment.season.label}")
                    ReadOnlyRow("Estilo", garment.style.label)
                    ColorRow(garment.primaryColor)
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    PixelButton(
                        text = "◀ Volver",
                        onClick = onBack,
                        background = PixelColors.WoodMid,
                        modifier = Modifier.weight(1f),
                    )
                    PixelButton(
                        text = "🗑 Eliminar",
                        onClick = { onDelete(garment) },
                        background = PixelColors.LabelRed,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
@Composable
private fun ReadOnlyRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
            color = PixelColors.LabelRed,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.width(96.dp),
        )
        Text(
            text = value,
            color = PixelColors.InkBrown,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
        )
    }
}

@Composable
private fun ColorRow(hex: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Color",
            color = PixelColors.LabelRed,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.width(96.dp),
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(parseHexColor(hex))
                .border(2.dp, PixelColors.WoodDark),
        )
        Text(
            text = hex,
            color = PixelColors.InkBrown,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
    }
}
