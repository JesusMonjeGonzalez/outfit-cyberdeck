package com.tinacyberdeck.outfit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinacyberdeck.outfit.ui.theme.PixelColors

/**
 * Una fila "Etiqueta ◄ Valor ►" — el bloque repetido del selector de
 * personaje: a la izquierda el nombre del atributo (en tinta roja, como
 * "Skin", "Hair", "Shirt" en Stardew), a la derecha flechas que ciclan el
 * valor actual.
 *
 * El valor usa `weight` para ocupar el espacio restante y encogerse cuando
 * la pantalla es estrecha, de modo que **ambas flechas quedan siempre
 * visibles** por muy poco ancho que haya (evita que la flecha derecha se
 * salga del borde en móviles verticales).
 */
@Composable
fun AttributeRow(
    label: String,
    value: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
            color = PixelColors.LabelRed,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(84.dp),
        )
        PixelArrowButton(direction = ArrowDirection.LEFT, onClick = onPrevious)
        Text(
            text = value,
            color = PixelColors.InkBrown,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        PixelArrowButton(direction = ArrowDirection.RIGHT, onClick = onNext)
    }
}
