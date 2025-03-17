package com.example.baato_assessment.views.actionButtons

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import com.example.baato_assessment.R
import com.example.baato_assessment.viewModel.CompassManager
import com.example.baato_assessment.viewModel.MapManager

@Composable
fun CustomCompass(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val rotationAngle = remember { mutableFloatStateOf(0f) }
    // start listening for orientation changes
    val compassManager = remember { CompassManager(context) }
    LaunchedEffect(Unit) { compassManager.startListening { angle -> rotationAngle.value = angle } }


    FloatingActionButton(R.drawable.compass,{ MapManager.resetCompass() },"", Modifier.rotate(rotationAngle.value)
    )

}
