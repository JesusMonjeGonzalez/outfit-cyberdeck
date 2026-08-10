package com.tinacyberdeck.outfit

import android.content.Context

/**
 * Guarda el [Context] de aplicación para que el código común pueda acceder a él
 * en Android (p. ej. para copiar imágenes a almacenamiento interno). Se
 * inicializa una única vez en `MainActivity.onCreate`.
 */
object AndroidAppContext {
    @Volatile
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun get(): Context =
        appContext ?: error("AndroidAppContext.init() no fue llamado todavía")
}
