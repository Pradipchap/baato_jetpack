package com.example.baato_assessment.api


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun reverseSearchRepository(
    lat: Double,
    lon: Double,
): ReverseSearchResponse {
    return withContext(Dispatchers.IO) {
        val queryParams = mutableMapOf<String, String>()
        queryParams["key"] = BAATO_API_KEY
        queryParams["lat"] = lat.toString()
        queryParams["lon"] = lon.toString()


        val response = ApiClient.baatoAPI.getReverseSearch(queryParams)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Null response body")
        } else {
            throw Exception("API request failed with status code: ${response.message()}")
        }
    }
}