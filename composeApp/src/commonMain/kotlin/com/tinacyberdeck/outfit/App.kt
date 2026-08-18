package com.tinacyberdeck.outfit

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinacyberdeck.outfit.data.db.OutfitDatabaseProvider
import com.tinacyberdeck.outfit.data.image.persistImageLocally
import com.tinacyberdeck.outfit.data.image.removePersistedImage
import com.tinacyberdeck.outfit.data.repository.OutfitRepository
import com.tinacyberdeck.outfit.data.repository.WardrobeRepository
import com.tinacyberdeck.outfit.domain.Garment
import com.tinacyberdeck.outfit.domain.Outfit
import com.tinacyberdeck.outfit.domain.OutfitGenerator
import com.tinacyberdeck.outfit.ui.screens.additem.AddGarmentScreen
import com.tinacyberdeck.outfit.ui.screens.detail.GarmentDetailScreen
import com.tinacyberdeck.outfit.ui.screens.favorites.FavoritesScreen
import com.tinacyberdeck.outfit.ui.screens.generator.GeneratorScreen
import com.tinacyberdeck.outfit.ui.screens.splash.SplashScreen
import com.tinacyberdeck.outfit.ui.screens.stats.StatsScreen
import com.tinacyberdeck.outfit.ui.screens.wardrobe.WardrobeScreen
import com.tinacyberdeck.outfit.ui.theme.PixelColors
import kotlinx.coroutines.launch

private enum class Screen { SPLASH, WARDROBE, ADD_GARMENT, GENERATOR, DETAIL, FAVORITES, STATS }

/**
 * Punto de entrada compartido. [pendingImagePath] llega desde la plataforma
 * (galería en Android, selector de archivo en Desktop) cuando el usuario elige
 * una foto para una prenda nueva; en cuanto tiene valor, la app navega a la
 * pantalla de alta.
 */
@Composable
fun App(
    repository: WardrobeRepository = remember { WardrobeRepository(OutfitDatabaseProvider.database) },
    outfitRepository: OutfitRepository = remember { OutfitRepository(OutfitDatabaseProvider.database) },
    pendingImagePath: String? = null,
    onImagePicked: () -> Unit = {},
    onRequestImagePick: () -> Unit = {},
) {
    MaterialTheme {
        var screen by remember { mutableStateOf(Screen.SPLASH) }
        var currentOutfit by remember { mutableStateOf<Outfit?>(null) }
        var selectedGarment by remember { mutableStateOf<Garment?>(null) }
        val garments by repository.observeAllGarments().collectAsState(initial = emptyList())
        val savedOutfits by outfitRepository.observeFavoriteOutfits().collectAsState(initial = emptyList())
        val scope = rememberCoroutineScope()
        val generator = remember { OutfitGenerator() }

        if (pendingImagePath != null && screen != Screen.ADD_GARMENT) {
            screen = Screen.ADD_GARMENT
        }

        val navVisible = screen == Screen.WARDROBE || screen == Screen.GENERATOR ||
            screen == Screen.FAVORITES || screen == Screen.STATS
        // Insets de barras del sistema + muesca de cámara: el contenido respeta
        // arriba/laterales siempre, y abajo solo cuando NO hay barra de navegación
        // (que ya se encarga ella del inset inferior).
        val barInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout)

        Column(modifier = Modifier.fillMaxSize().background(PixelColors.WoodDark)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        if (navVisible) {
                            barInsets.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                        } else {
                            barInsets
                        },
                    ),
            ) {
                Crossfade(targetState = screen, animationSpec = tween(260), label = "screenTransition") { current ->
                    when (current) {
                    Screen.SPLASH -> SplashScreen(onDone = { screen = Screen.WARDROBE })

                    Screen.WARDROBE -> WardrobeScreen(
                        garments = garments,
                        onAddClick = onRequestImagePick,
                        onGenerateClick = { screen = Screen.GENERATOR },
                        onGarmentClick = { garment ->
                            selectedGarment = garment
                            screen = Screen.DETAIL
                        },
                    )

                    Screen.ADD_GARMENT -> {
                        val path = pendingImagePath
                        if (path != null) {
                            AddGarmentScreen(
                                imagePath = path,
                                onConfirm = { state ->
                                    scope.launch {
                                        // Copia física a almacenamiento de la app antes de guardar,
                                        // para no depender del permiso temporal de la URI original.
                                        val persisted = persistImageLocally(state.imagePath)
                                        repository.addGarment(state.toDraft().copy(imagePath = persisted))
                                        onImagePicked()
                                        screen = Screen.WARDROBE
                                    }
                                },
                                onCancel = {
                                    onImagePicked()
                                    screen = Screen.WARDROBE
                                },
                            )
                        }
                    }

                    Screen.GENERATOR -> GeneratorScreen(
                        outfit = currentOutfit,
                        onGenerateClick = { style, season ->
                            currentOutfit = generator.generate(garments, style, season)
                        },
                        onSaveClick = {
                            val outfit = currentOutfit
                            if (outfit != null) {
                                scope.launch { outfitRepository.saveFavorite(outfit) }
                            }
                        },
                    )

                    Screen.DETAIL -> {
                        val garment = selectedGarment
                        if (garment != null) {
                            GarmentDetailScreen(
                                garment = garment,
                                onBack = { screen = Screen.WARDROBE },
                                onDelete = { toDelete ->
                                    scope.launch {
                                        repository.deleteGarment(toDelete.id)
                                        removePersistedImage(toDelete.imagePath)
                                        selectedGarment = null
                                        screen = Screen.WARDROBE
                                    }
                                },
                            )
                        }
                    }

                    Screen.FAVORITES -> FavoritesScreen(
                        outfits = savedOutfits,
                        onDeleteOutfit = { outfit ->
                            scope.launch { outfitRepository.deleteOutfit(outfit.id) }
                        },
                    )

                    Screen.STATS -> StatsScreen(garments = garments)
                    }
                }
            }

            if (navVisible) {
                BottomNav(
                    current = screen,
                    onSelect = { screen = it },
                    modifier = Modifier.windowInsetsPadding(
                        barInsets.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
                    ),
                )
            }
        }
    }
}

@Composable
private fun BottomNav(current: Screen, onSelect: (Screen) -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(PixelColors.WoodDark)
            .height(62.dp),
    ) {
        NavItem("👗", "Armario", current == Screen.WARDROBE, Modifier.weight(1f)) { onSelect(Screen.WARDROBE) }
        NavItem("🎲", "Generar", current == Screen.GENERATOR, Modifier.weight(1f)) { onSelect(Screen.GENERATOR) }
        NavItem("⭐", "Favoritos", current == Screen.FAVORITES, Modifier.weight(1f)) { onSelect(Screen.FAVORITES) }
        NavItem("📊", "Stats", current == Screen.STATS, Modifier.weight(1f)) { onSelect(Screen.STATS) }
        // Cambia de tema (Granja ⇄ Kawaii): el label muestra el tema activo.
        NavItem("🎨", PixelColors.activeThemeName, false, Modifier.weight(1f)) { PixelColors.toggle() }
    }
}

@Composable
private fun NavItem(
    icon: String,
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxHeight().clickable(onClick = onClick),
    ) {
        Text(text = icon, fontSize = 19.sp)
        Text(
            text = label,
            color = if (selected) PixelColors.AccentOrange else PixelColors.ParchmentLight,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 10.sp,
            maxLines = 1,
        )
    }
}
