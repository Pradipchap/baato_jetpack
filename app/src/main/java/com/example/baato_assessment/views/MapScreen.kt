package com.example.baato_assessment.views

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.baato_assessment.viewModel.MapManager
import com.example.baato_assessment.views.actionButtons.CustomCompass
import com.example.baato_assessment.views.actionButtons.FloatingButtons
import com.example.baato_assessment.views.icons.My_location
import org.maplibre.android.maps.MapView

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val mapManager = remember { MapManager(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    mapManager.initializeMap(this) { }
                    mapManager.initializeLocationEngine()

                    //after initialization go to current user location and zoom in
                    mapManager.goToUserLocation()
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        FloatingButtons(mapManager)
    }
}
