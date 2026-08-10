package com.tinacyberdeck.outfit.domain

/**
 * Cada enum representa un "atributo" navegable con flechas ◄ ▶
 * en la pantalla de creación de prenda, al estilo del selector
 * de personaje de Stardew Valley.
 */
enum class Category(val label: String, val emoji: String) {
    TOP("Top", "👕"),
    BOTTOM("Pantalón / Falda", "👖"),
    FOOTWEAR("Calzado", "👟"),
    OUTERWEAR("Abrigo / Chaqueta", "🧥"),
    ACCESSORY("Accesorio", "💍"),
    BAG("Bolso", "👜");

    companion object {
        fun cycle(current: Category, forward: Boolean): Category {
            val values = entries
            val idx = values.indexOf(current)
            val nextIdx = if (forward) (idx + 1) % values.size else (idx - 1 + values.size) % values.size
            return values[nextIdx]
        }
    }
}

enum class Fit(val label: String) {
    REGULAR("Regular"),
    HOLGADO("Holgado"),
    CEÑIDO("Ceñido"),
    OVERSIZE("Oversize");

    companion object {
        fun cycle(current: Fit, forward: Boolean): Fit {
            val values = entries
            val idx = values.indexOf(current)
            val nextIdx = if (forward) (idx + 1) % values.size else (idx - 1 + values.size) % values.size
            return values[nextIdx]
        }
    }
}

enum class Season(val label: String, val emoji: String) {
    TODO_EL_AÑO("Todo el año", "🌤️"),
    VERANO("Verano", "☀️"),
    INVIERNO("Invierno", "❄️"),
    ENTRETIEMPO("Entretiempo", "🍂");

    companion object {
        fun cycle(current: Season, forward: Boolean): Season {
            val values = entries
            val idx = values.indexOf(current)
            val nextIdx = if (forward) (idx + 1) % values.size else (idx - 1 + values.size) % values.size
            return values[nextIdx]
        }
    }
}

enum class Style(val label: String) {
    CASUAL("Casual"),
    FORMAL("Formal"),
    DEPORTE("Deporte"),
    GOING_OUT("Going out"),
    TRABAJO("Trabajo");

    companion object {
        fun cycle(current: Style, forward: Boolean): Style {
            val values = entries
            val idx = values.indexOf(current)
            val nextIdx = if (forward) (idx + 1) % values.size else (idx - 1 + values.size) % values.size
            return values[nextIdx]
        }
    }
}

/** Tipos de prenda específicos por categoría — la lista cambia según la categoría elegida. */
object GarmentTypes {
    private val byCategory: Map<Category, List<String>> = mapOf(
        Category.TOP to listOf("Camiseta", "Camisa", "Top", "Sudadera", "Jersey", "Blusa", "Crop top"),
        Category.BOTTOM to listOf("Vaquero", "Pantalón de vestir", "Falda", "Short", "Legging", "Jogger"),
        Category.FOOTWEAR to listOf("Zapatilla", "Bota", "Tacón", "Sandalia", "Mocasín", "Zapato de vestir"),
        Category.OUTERWEAR to listOf("Chaqueta", "Abrigo", "Cazadora", "Blazer", "Gabardina", "Chaleco"),
        Category.ACCESSORY to listOf("Collar", "Pulsera", "Gafas de sol", "Cinturón", "Gorro", "Bufanda"),
        Category.BAG to listOf("Bolso de mano", "Mochila", "Bandolera", "Riñonera", "Tote bag"),
    )

    fun forCategory(category: Category): List<String> = byCategory[category].orEmpty()

    fun cycle(category: Category, current: String, forward: Boolean): String {
        val options = forCategory(category)
        if (options.isEmpty()) return current
        val idx = options.indexOf(current).let { if (it == -1) 0 else it }
        val nextIdx = if (forward) (idx + 1) % options.size else (idx - 1 + options.size) % options.size
        return options[nextIdx]
    }
}
