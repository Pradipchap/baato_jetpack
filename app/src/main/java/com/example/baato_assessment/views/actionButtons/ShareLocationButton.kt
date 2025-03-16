package com.example.baato_assessment.views.actionButtons

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.baato_assessment.R
import com.example.baato_assessment.viewModel.MapManager

@Composable
fun ShareLocationButton(){
    val context = LocalContext.current // getting the current context for starting activity to share location

    fun shareLocation() {
        val latlong= ""
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
    FloatingActionButton(
        icon = R.drawable.ic_launcher_foreground,
        onClick = { shareLocation() }
    )
}