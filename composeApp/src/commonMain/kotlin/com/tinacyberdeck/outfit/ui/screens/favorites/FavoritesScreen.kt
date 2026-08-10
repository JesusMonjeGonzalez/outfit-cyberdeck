package com.tinacyberdeck.outfit.ui.screens.favorites

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tinacyberdeck.outfit.domain.Outfit
import com.tinacyberdeck.outfit.ui.components.MascotHelper
import com.tinacyberdeck.outfit.ui.theme.PixelColors

/**
 * Lista de outfits guardados como favoritos, cada uno con su nombre y una fila
 * de miniaturas de sus prendas. Permite borrar cada favorito.
 */
@Composable
fun FavoritesScreen(
    outfits: List<Outfit>,
    onDeleteOutfit: (Outfit) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PixelColors.WoodDark)
            .padding(12.dp)
            .background(PixelColors.Parchment)
            .border(3.dp, PixelColors.WoodMid)
            .padding(12.dp),
    ) {
        if (outfits.isEmpty()) {
            MascotHelper(
                message = "Aún no hay favoritos.\nGenera un look y pulsa ★ y lo guardo aquí.",
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(outfits, key = { it.id }) { outfit ->
                    FavoriteCard(outfit = outfit, onDelete = { onDeleteOutfit(outfit) })
                }
            }
        }
    }
}

@Composable
private fun FavoriteCard(outfit: Outfit, onDelete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PixelColors.WoodDark)
            .padding(4.dp)
            .background(PixelColors.ParchmentLight)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "★ ${outfit.name}",
                color = PixelColors.LabelRed,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f),
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(32.dp)
                    .clip(CutCornerShape(6.dp))
                    .background(PixelColors.LabelRed)
                    .clickable(onClick = onDelete)
                    .padding(horizontal = 10.dp),
            ) {
                Text("🗑", fontSize = 14.sp, color = PixelColors.ParchmentLight)
            }
        }

        val pieces = outfit.pieces()
        if (pieces.isEmpty()) {
            Text(
                text = "Este outfit ya no tiene prendas (fueron eliminadas).",
                color = PixelColors.InkBrown,
                fontSize = 13.sp,
            )
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(pieces, key = { it.id }) { garment ->
                    Box(
                        modifier = Modifier
                            .height(96.dp)
                            .aspectRatio(0.8f)
                            .background(PixelColors.WoodDark)
                            .padding(3.dp)
                            .background(PixelColors.Parchment),
                    ) {
                        AsyncImage(
                            model = garment.imagePath,
                            contentDescription = garment.garmentType,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                        Text(
                            text = garment.category.emoji,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.TopEnd).padding(2.dp),
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(2.dp))
    }
}
