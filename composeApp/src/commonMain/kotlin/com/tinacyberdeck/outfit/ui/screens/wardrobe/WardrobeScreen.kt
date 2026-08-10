package com.tinacyberdeck.outfit.ui.screens.wardrobe

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tinacyberdeck.outfit.domain.Category
import com.tinacyberdeck.outfit.domain.Garment
import com.tinacyberdeck.outfit.ui.components.MascotHelper
import com.tinacyberdeck.outfit.ui.theme.PixelColors
import com.tinacyberdeck.outfit.ui.theme.accent

@Composable
fun WardrobeScreen(
    garments: List<Garment>,
    onAddClick: () -> Unit,
    onGenerateClick: () -> Unit,
    onGarmentClick: (Garment) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var categoryFilter by remember { mutableStateOf<Category?>(null) }

    val filtered = garments.filter { g ->
        (categoryFilter == null || g.category == categoryFilter) &&
            (query.isBlank() ||
                g.garmentType.contains(query, ignoreCase = true) ||
                g.category.label.contains(query, ignoreCase = true) ||
                g.style.label.contains(query, ignoreCase = true))
    }

    Scaffold(
        containerColor = PixelColors.WoodDark,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PixelColors.AccentOrange,
                shape = CutCornerShape(10.dp),
            ) {
                Text("+", fontSize = 24.sp, color = PixelColors.ParchmentLight, fontWeight = FontWeight.Bold)
            }
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (garments.isEmpty()) {
                EmptyWardrobe(onAddClick = onAddClick)
            } else {
                MascotHelper(
                    message = if (filtered.size == garments.size) {
                        "Tienes ${garments.size} prenda${if (garments.size == 1) "" else "s"}. Toca una para ver su ficha ✨"
                    } else {
                        "${filtered.size} coincidencia${if (filtered.size == 1) "" else "s"} de ${garments.size}"
                    },
                    compact = true,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
                SearchField(query = query, onQueryChange = { query = it })
                CategoryChips(selected = categoryFilter, onSelect = { categoryFilter = it })

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .background(PixelColors.Parchment)
                        .border(3.dp, PixelColors.WoodMid)
                        .padding(12.dp),
                ) {
                    if (filtered.isEmpty()) {
                        Text(
                            text = "No hay prendas que coincidan con tu búsqueda.",
                            color = PixelColors.InkBrown,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(4.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(filtered, key = { it.id }) { garment ->
                                GarmentThumbnail(
                                    garment = garment,
                                    onClick = { onGarmentClick(garment) },
                                    modifier = Modifier.animateItem(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyWardrobe(onAddClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(PixelColors.Parchment)
            .border(3.dp, PixelColors.WoodMid)
            .padding(20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.align(Alignment.Center),
        ) {
            MascotHelper(message = "¡Hola! Soy Aguja.\nEmpecemos tu armario 🪡")
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CutCornerShape(10.dp))
                    .background(PixelColors.AccentOrange)
                    .border(2.dp, PixelColors.WoodDark, CutCornerShape(10.dp))
                    .clickable(onClick = onAddClick)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
            ) {
                Text(
                    text = "➕ Añadir mi primera prenda",
                    color = PixelColors.ParchmentLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                )
            }
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(CutCornerShape(8.dp))
            .background(PixelColors.ParchmentLight)
            .border(2.dp, PixelColors.WoodDark, CutCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = TextStyle(color = PixelColors.InkBrown, fontSize = 15.sp),
            cursorBrush = SolidColor(PixelColors.AccentOrange),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔍  ", fontSize = 14.sp)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (query.isEmpty()) {
                            Text(
                                text = "Buscar por tipo, categoría o estilo…",
                                color = PixelColors.InkBrown.copy(alpha = 0.5f),
                                fontSize = 15.sp,
                            )
                        }
                        inner()
                    }
                }
            },
        )
    }
}

@Composable
private fun CategoryChips(selected: Category?, onSelect: (Category?) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Chip(label = "Todo", tint = PixelColors.WoodMid, selected = selected == null, onClick = { onSelect(null) })
        Category.entries.forEach { category ->
            Chip(
                label = "${category.emoji} ${category.label}",
                tint = category.accent(),
                selected = selected == category,
                onClick = { onSelect(if (selected == category) null else category) },
            )
        }
    }
}

@Composable
private fun Chip(label: String, tint: androidx.compose.ui.graphics.Color, selected: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(if (selected) tint else PixelColors.ParchmentLight, label = "chipBg")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CutCornerShape(6.dp))
            .background(bg)
            .border(2.dp, if (selected) PixelColors.WoodDark else tint, CutCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = label,
            color = if (selected) PixelColors.ParchmentLight else PixelColors.InkBrown,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
        )
    }
}

@Composable
private fun GarmentThumbnail(garment: Garment, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(0.85f)
            .background(PixelColors.WoodDark)
            .padding(4.dp)
            .background(PixelColors.ParchmentLight)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = garment.imagePath,
            contentDescription = garment.garmentType,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Text(
            text = garment.category.emoji,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.TopEnd).padding(2.dp),
        )
    }
}
