package com.tinacyberdeck.outfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import com.tinacyberdeck.outfit.data.db.DatabaseDriverFactory
import com.tinacyberdeck.outfit.data.db.OutfitDatabaseProvider

class MainActivity : ComponentActivity() {

    // Ruta de la foto elegida, compartida entre el callback del picker y Compose.
    private val pendingImagePath = mutableStateOf<String?>(null)

    // IMPORTANTE: el launcher debe registrarse antes de que la Activity esté
    // STARTED, por eso va como campo (se inicializa en la construcción), no
    // dentro de setContent (que corre ya en estado RESUMED y lanzaba
    // IllegalStateException).
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                pendingImagePath.value = uri.toString()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        AndroidAppContext.init(applicationContext)
        OutfitDatabaseProvider.initialize(DatabaseDriverFactory(applicationContext))

        setContent {
            App(
                pendingImagePath = pendingImagePath.value,
                onImagePicked = { pendingImagePath.value = null },
                onRequestImagePick = { pickImage.launch("image/*") },
            )
        }
    }
}
