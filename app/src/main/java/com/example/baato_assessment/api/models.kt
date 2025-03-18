package com.example.baato_assessment.api

data class NearbyPlacesResponse(
    val timestamp: String,
    val status: Int,
    val message: String,
    val data: List<NearbyPlace>
)

data class NearbyPlace(
    val placeId: Int,
    val osmId: Long,
    val name: String,
    val address: String,
    val type: String,
    val centroid: Centroid,
    val tags: List<String>?,
    val geometry: Geometry?,
    val score: Double?,
    val radialDistanceInKm: Double?,
    val open: Boolean?
)

data class Centroid(
    val lat: Double,
    val lon: Double
)

data class Geometry(
    val type: String,
    val coordinates: Any?
)

data class Category(val label: String, val value: String)
