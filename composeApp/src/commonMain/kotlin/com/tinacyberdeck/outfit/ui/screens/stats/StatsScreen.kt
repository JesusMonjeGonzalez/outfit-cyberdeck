package com.tinacyberdeck.outfit.ui.screens.stats

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinacyberdeck.outfit.domain.Category
import com.tinacyberdeck.outfit.domain.Garment
import com.tinacyberdeck.outfit.ui.components.MascotHelper
import com.tinacyberdeck.outfit.ui.components.WoodFrame
import com.tinacyberdeck.outfit.ui.theme.PixelColors
import com.tinacyberdeck.outfit.ui.theme.accent
import com.tinacyberdeck.outfit.ui.theme.parseHexColor

/**
 * Panel de "Tu armario en números": total de prendas, reparto por categoría con
 * barras que crecen al entrar, y la paleta real de colores de tus prendas. Se
 * calcula todo al vuelo desde el armario, sin datos nuevos.
 */
@Composable
fun StatsScreen(garments: List<Garment>) {
    val countByCategory = remember(garments) {
        Category.entries.associateWith { cat -> garments.count { it.category == cat } }
    }
    val maxCount = (countByCategory.values.maxOrNull() ?: 0).coerceAtLeast(1)
    val topCategory = countByCategory.filterValues { it > 0 }.maxByOrNull { it.value }?.key

    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                    text = "Tu armario en números",
                    color = PixelColors.LabelRed,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                )

                MascotHelper(
                    message = if (garments.isEmpty()) {
                        "Añade prendas y te enseño tus números 📊"
                    } else {
                        "Tu categoría estrella es ${topCategory?.emoji} ${topCategory?.label} 👑"
                    },
                    compact = true,
                )

                // Total grande.
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${garments.size}",
                        color = PixelColors.AccentOrange,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 48.sp,
                    )
                    Text(
                        text = "prenda${if (garments.size == 1) "" else "s"}",
                        color = PixelColors.InkBrown,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 10.dp),
                    )
                }

                Text(
                    text = "Por categoría",
                    color = PixelColors.LabelRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Category.entries.forEach { category ->
                        val count = countByCategory[category] ?: 0
                        CategoryBar(
                            label = "${category.emoji} ${category.label}",
                            count = count,
                            fraction = count.toFloat() / maxCount,
                            color = category.accent(),
                        )
                    }
                }

                if (garments.isNotEmpty()) {
                    Text(
                        text = "Tu paleta",
                        color = PixelColors.LabelRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                    ) {
                        garments.forEach { g ->
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CutCornerShape(4.dp))
                                    .background(parseHexColor(g.primaryColor))
                                    .border(2.dp, PixelColors.WoodDark, CutCornerShape(4.dp)),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryBar(label: String, count: Int, fraction: Float, color: androidx.compose.ui.graphics.Color) {
    var play by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { play = true }
    val animated by animateFloatAsState(
        targetValue = if (play) fraction else 0f,
        animationSpec = tween(650),
        label = "bar",
    )

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            color = PixelColors.InkBrown,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.width(140.dp),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .background(PixelColors.ParchmentLight)
                .border(2.dp, PixelColors.WoodDark),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animated.coerceIn(0f, 1f))
                    .height(20.dp)
                    .background(color),
            )
        }
        Text(
            text = "$count",
            color = PixelColors.InkBrown,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.width(24.dp),
        )
    }
}
