package com.example.baato_assessment.views.actionButtons

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.pradipchapagain.baato_assessment.R
import com.example.baato_assessment.viewModel.MapManager

@Composable
fun ShareLocationButton() {
    val context =
        LocalContext.current // getting the current context for starting activity to share location

    fun shareLocation() {

        MapManager.getUserLocation { latLng ->
            if (latLng != null) {
                val lat = latLng.latitude.toString()
                val lng = latLng.longitude.toString()
                val deepLinkUri = Uri.Builder()
                    .scheme("https")
                    .authority("pradipchapagain.com.np")
                    .path("/location")
                    .appendQueryParameter("lat", lat.toString())
                    .appendQueryParameter("lng", lng.toString())
                    .build()

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, deepLinkUri.toString())
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }
        }

    }
    FloatingActionButton(
        icon = R.drawable.share,
        onClick = { shareLocation() },
        containerColor = MaterialTheme.colorScheme.background
    )
}