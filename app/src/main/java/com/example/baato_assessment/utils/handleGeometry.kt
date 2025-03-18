package com.example.baato_assessment.utils

import com.example.baato_assessment.api.Geometry
import org.json.JSONObject
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource

fun handleGeometry(geometry: Geometry?, map: MapLibreMap) {
    geometry?.let {
        if (it.type == "Polygon") {
            val coordinates = it.coordinates as? List<List<List<Double>>> // Cast to expected type
            coordinates?.let { polygonCoords ->
                // Create a GeoJSON Polygon Feature
                val polygon = JSONObject()
                polygon.put("type", "Polygon")
                polygon.put("coordinates", polygonCoords)

                val feature = JSONObject()
                feature.put("type", "Feature")
                feature.put("geometry", polygon)

                // Create a GeoJsonSource for the polygon
                val sourceId = "polygon-source-${System.currentTimeMillis()}"
                val source = GeoJsonSource(sourceId, feature.toString())
                map.getStyle()?.addSource(source)

                // Create a FillLayer to draw the polygon
                val layerId = "polygon-layer-${System.currentTimeMillis()}"
                val layer = org.maplibre.android.style.layers.FillLayer(layerId, sourceId)
                layer.setProperties(
                    PropertyFactory.fillColor(android.graphics.Color.parseColor("#800000FF")), // Example: Blue fill with transparency
                    PropertyFactory.fillOpacity(0.5f)
                )
                map.getStyle()?.addLayer(layer)
            }
        } else {
            // Handle other geometry types (e.g., Point)
            // if it is a point, then it is already handled by the add marker function.
        }
    }
}