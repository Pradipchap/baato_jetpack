package com.example.baato_assessment.utils

import org.maplibre.android.geometry.LatLng

fun calculateRadius(southWest: LatLng, northEast: LatLng): Int {
    val distance = southWest.distanceTo(northEast) / 2000
    return distance.toInt()
}