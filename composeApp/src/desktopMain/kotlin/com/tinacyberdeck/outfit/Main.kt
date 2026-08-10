package com.tinacyberdeck.outfit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.tinacyberdeck.outfit.data.db.DatabaseDriverFactory
import com.tinacyberdeck.outfit.data.db.OutfitDatabaseProvider
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

/**
 * Punto de entrada del target Desktop (JVM). Inicializa la base de datos
 * local, abre la ventana principal y conecta el "photo picker" a un
 * [FileDialog] nativo del sistema para elegir la foto de cada prenda.
 */
fun main() {
    OutfitDatabaseProvider.initialize(DatabaseDriverFactory())

    application {
        val windowState = rememberWindowState(size = DpSize(900.dp, 700.dp))
        var pendingImagePath by remember { mutableStateOf<String?>(null) }

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Outfit Cyberdeck",
        ) {
            App(
                pendingImagePath = pendingImagePath,
                onImagePicked = { pendingImagePath = null },
                onRequestImagePick = { pendingImagePath = pickImageFile() },
            )
        }
    }
}

/**
 * Abre un selector de archivo nativo filtrando por imágenes y devuelve la
 * ruta elegida como URI `file:` (formato que Coil sabe resolver en JVM),
 * o `null` si el usuario cancela.
 */
private fun pickImageFile(): String? {
    val dialog = FileDialog(null as Frame?, "Elige una foto de prenda", FileDialog.LOAD)
    dialog.setFilenameFilter { _, name ->
        name.lowercase().substringAfterLast('.') in setOf("jpg", "jpeg", "png", "webp", "gif", "bmp")
    }
    dialog.isVisible = true

    val dir = dialog.directory ?: return null
    val file = dialog.file ?: return null
    return File(dir, file).toURI().toString()
}
