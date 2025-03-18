package com.example.baato_assessment.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface BaatoAPI {
    @GET("search/nearby")
    suspend fun getNearbyPlaces(
        @QueryMap parameters: Map<String, String>
    ): Response<NearbyPlacesResponse>

    @GET("reverse")
    suspend fun getReverseSearch(
        @QueryMap parameters: Map<String, String>
    ): Response<ReverseSearchResponse>
}
