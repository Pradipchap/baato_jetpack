package com.example.baato_assessment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.baato_assessment.ui.theme.BaatoassessmentTheme
import com.example.baato_assessment.viewModel.MapManager
import com.example.baato_assessment.views.MapScreen
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // initializing the MapLibre before using MapView
        MapLibre.getInstance(this, "bpk.YRfF8dHCw5QDEJUD3mOy-I3SdH52xqiD-BMG0iq3FgAZ",
            WellKnownTileServer.MapLibre)

        try {
            setContent {
                BaatoassessmentTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) {
                        MapScreen()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate: ${e.localizedMessage}", e)
        }
    }
}


