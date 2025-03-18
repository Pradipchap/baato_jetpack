package com.example.baato_assessment.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getNearbyPlaces(
    lat: Double,
    lon: Double,
    type: String? = null,
    radius: Int? = null,
    limit: Int? = null,
    sortBy: Boolean? = null,
    isOpen: Boolean? = null
): NearbyPlacesResponse {
    return withContext(Dispatchers.IO) {
        val queryParams = mutableMapOf<String, String>()
        queryParams["key"] = BAATO_API_KEY
        queryParams["lat"] = lat.toString()
        queryParams["lon"] = lon.toString()
        type?.let { queryParams["type"] = it }


        val response = ApiClient.baatoAPI.getNearbyPlaces(queryParams)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Null response body")
        } else {
            throw Exception("API request failed with status code: ${response.message()}")
        }
    }
}