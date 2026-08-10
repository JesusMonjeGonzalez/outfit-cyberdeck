package com.tinacyberdeck.outfit.data.image

import android.net.Uri
import com.tinacyberdeck.outfit.AndroidAppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual suspend fun persistImageLocally(source: String): String = withContext(Dispatchers.IO) {
    val context = AndroidAppContext.get()
    val resolver = context.contentResolver
    val uri = Uri.parse(source)

    // Conserva la extensión real (webp, png, gif...) a partir del MIME.
    val extension = when (val subtype = resolver.getType(uri)?.substringAfterLast('/')) {
        "jpeg" -> "jpg"
        null, "" -> "img"
        else -> subtype
    }

    val dir = File(context.filesDir, "garments").apply { if (!exists()) mkdirs() }
    val dest = File(dir, "garment_${System.currentTimeMillis()}.$extension")

    resolver.openInputStream(uri).use { input ->
        requireNotNull(input) { "No se pudo abrir la imagen de origen: $source" }
        dest.outputStream().use { output -> input.copyTo(output) }
    }

    dest.toURI().toString()
}
