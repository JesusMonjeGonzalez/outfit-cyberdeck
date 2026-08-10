package com.tinacyberdeck.outfit.ui.screens.splash

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinacyberdeck.outfit.ui.components.MascotSprite
import com.tinacyberdeck.outfit.ui.theme.PixelColors
import kotlinx.coroutines.delay

/**
 * Pantalla de bienvenida: Aguja entra dando un saltito elástico con el título
 * y, tras un momento, cede el paso al armario.
 */
@Composable
fun SplashScreen(onDone: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.5f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "splashScale",
    )
    val fade by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label = "splashFade",
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(1600)
        onDone()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(PixelColors.WoodDark),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.alpha(fade),
        ) {
            MascotSprite(
                size = 140.dp,
                modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale },
            )
            Text(
                text = "Outfit Cyberdeck",
                color = PixelColors.LabelRed,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "tu armario pixel 🪡",
                color = PixelColors.ParchmentLight,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}
