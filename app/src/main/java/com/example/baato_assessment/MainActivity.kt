package com.example.baato_assessment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.baato_assessment.api.BAATO_API_KEY
import com.example.baato_assessment.ui.theme.BaatoassessmentTheme
import com.example.baato_assessment.viewModel.DeepLinkHandler
import com.example.baato_assessment.viewModel.MapManager
import com.example.baato_assessment.views.MapScreen
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // initializing the MapLibre before using MapView
        MapLibre.getInstance(
            this, BAATO_API_KEY,
            WellKnownTileServer.MapLibre
        )

        MapManager.initialize(this)

        try {
            setContent {
                BaatoassessmentTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) {
                        MapScreen(onMapReady = { mapLibreMap ->
                            DeepLinkHandler.handleDeepLink(intent)
                        }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate: ${e.localizedMessage}", e)
        }
    }
}


