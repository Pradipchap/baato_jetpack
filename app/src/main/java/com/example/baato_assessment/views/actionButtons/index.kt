package com.example.baato_assessment.views.actionButtons

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.baato_assessment.viewModel.MapManager
import com.example.baato_assessment.views.icons.My_location

@Composable
fun FloatingButtons(
    mapManager: MapManager,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), // Add spacing between buttons
            horizontalAlignment = Alignment.End // Ensure buttons align to the right
        ) {
            CustomCompass(mapManager) //Assuming custom compass is a composable
            FloatingActionButton(
                icon = My_location,
                onClick = { mapManager.goToUserLocation() }
            )
        }
    }
}