package com.tinacyberdeck.outfit.ui.preview

import androidx.compose.runtime.Composable
import com.tinacyberdeck.outfit.ui.screens.detail.GarmentDetailScreen
import com.tinacyberdeck.outfit.ui.screens.favorites.FavoritesScreen
import com.tinacyberdeck.outfit.ui.screens.generator.GeneratorScreen
import com.tinacyberdeck.outfit.ui.screens.stats.StatsScreen
import com.tinacyberdeck.outfit.ui.screens.wardrobe.WardrobeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun WardrobeScreenPreview() {
    WardrobeScreen(
        garments = sampleGarments,
        onAddClick = {},
        onGenerateClick = {},
        onGarmentClick = {},
    )
}

@Preview
@Composable
private fun WardrobeEmptyPreview() {
    WardrobeScreen(
        garments = emptyList(),
        onAddClick = {},
        onGenerateClick = {},
        onGarmentClick = {},
    )
}

@Preview
@Composable
private fun GeneratorScreenPreview() {
    GeneratorScreen(
        outfit = sampleOutfit,
        onGenerateClick = { _, _ -> },
        onSaveClick = {},
    )
}

@Preview
@Composable
private fun GarmentDetailPreview() {
    GarmentDetailScreen(
        garment = sampleGarments.first(),
        onBack = {},
        onDelete = {},
    )
}

@Preview
@Composable
private fun FavoritesScreenPreview() {
    FavoritesScreen(
        outfits = listOf(sampleOutfit),
        onDeleteOutfit = {},
    )
}

@Preview
@Composable
private fun StatsScreenPreview() {
    StatsScreen(garments = sampleGarments)
}
