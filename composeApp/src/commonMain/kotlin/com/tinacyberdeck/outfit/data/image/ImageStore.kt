package com.tinacyberdeck.outfit.data.image

/**
 * Copia la imagen indicada por [source] (una URI `content://` en Android o
 * `file:` en Desktop) al almacenamiento local de la app y devuelve una ruta/URI
 * estable que se puede volver a cargar más tarde sin depender de permisos
 * temporales sobre la URI original.
 *
 * Formatos admitidos: los que sepa decodificar Coil (png, jpg/jpeg, webp, gif,
 * bmp; en Android también heic/heif). Se copian los bytes sin recomprimir.
 */
expect suspend fun persistImageLocally(source: String): String

/** Elimina una imagen que Outfit creó dentro de su propio almacenamiento. */
expect suspend fun removePersistedImage(path: String)
