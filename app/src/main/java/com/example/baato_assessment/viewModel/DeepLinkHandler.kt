package com.example.baato_assessment.viewModel

import android.content.Intent
import org.maplibre.android.geometry.LatLng

object DeepLinkHandler {

    fun handleDeepLink(intent: Intent?) {
        if (intent !== null) {

            val action = intent.action
            val data = intent.data

            if (action == Intent.ACTION_VIEW && data != null) {
                val scheme = data.scheme
                val host = data.host
                val path = data.path

                if (scheme == "https" && host == "pradipchapagain.com.np" && path == "/location") {
                    val lat = data.getQueryParameter("lat")?.toDoubleOrNull()
                    val lng = data.getQueryParameter("lng")?.toDoubleOrNull()

                    if (lat != null && lng != null) {
                        val latLng = LatLng(lat, lng)
                        MapManager.addRedMarker(latLng)
                    }
                }
            } else {
                MapManager.goToUserLocation()
                return
            }
        } else {
            MapManager.goToUserLocation()
            return
        }

    }
}