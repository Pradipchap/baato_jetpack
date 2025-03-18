package com.example.baato_assessment.views

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.baato_assessment.viewModel.DeepLinkHandler
import com.example.baato_assessment.viewModel.MapManager
import com.example.baato_assessment.views.actionButtons.FloatingButtons
import com.example.baato_assessment.views.popups.CategorySelectorPreview
import com.example.baato_assessment.views.popups.CustomBottomSheet
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView

@Composable
fun MapScreen(onMapReady: (MapLibreMap) -> Unit) {
    CategorySelectorPreview()

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    MapManager.initializeMap(this) { mapLibreMap ->
                        // Notify when map is ready
                        onMapReady(mapLibreMap)
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        FloatingButtons()
        CustomBottomSheet()
    }
}
