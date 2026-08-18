package com.tinacyberdeck.outfit.data.image

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URI

actual suspend fun persistImageLocally(source: String): String = withContext(Dispatchers.IO) {
    // El FileDialog nativo entrega una URI `file:` a un fichero estable; lo
    // copiamos a una carpeta de la app para no depender de su ubicación original.
    val srcFile = if (source.startsWith("file:")) File(URI(source)) else File(source)
    val dir = File(System.getProperty("user.home"), ".outfit-cyberdeck/garments")
        .apply { if (!exists()) mkdirs() }
    val dest = File(dir, "garment_${System.currentTimeMillis()}_${srcFile.name}")
    srcFile.copyTo(dest, overwrite = true)
    dest.toURI().toString()
}

actual suspend fun removePersistedImage(path: String) = withContext(Dispatchers.IO) {
    val root = File(System.getProperty("user.home"), ".outfit-cyberdeck/garments").canonicalFile
    val target = runCatching { File(URI(path)).canonicalFile }.getOrNull() ?: return@withContext
    if (target.parentFile == root || target.path.startsWith(root.path + File.separator)) {
        target.delete()
    }
}
