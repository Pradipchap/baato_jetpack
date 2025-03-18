package com.example.baato_assessment.viewModel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.baato_assessment.api.BAATO_API_KEY
import com.example.baato_assessment.api.NearbyPlace
import com.example.baato_assessment.api.getNearbyPlaces
import com.example.baato_assessment.utils.calculateRadius
import com.example.baato_assessment.utils.handleGeometry
import com.google.android.gms.location.FusedLocationProviderClient
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import com.google.android.gms.location.LocationServices
import org.maplibre.android.location.engine.LocationEngineRequest
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import org.maplibre.android.style.layers.PropertyFactory.*
import com.pradipchapagain.baato_assessment.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


//use this class to manage map from anywhere in the app
object MapManager {

    private var appContext: Context? = null
    private var mapView: MapView? = null
    var map: MapLibreMap? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    fun initialize(applicationContext: Context) {
        this.appContext = applicationContext
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        initializeLocationEngine()
    }

    fun initializeMap(mapView: MapView, onMapReady: (MapLibreMap) -> Unit) {
        this.mapView = mapView
        mapView.getMapAsync { mapLibreMap ->
            this.map = mapLibreMap
            mapLibreMap.uiSettings.isCompassEnabled = false
            mapLibreMap.uiSettings.setCompassFadeFacingNorth(false)
            mapLibreMap.uiSettings.compassGravity = Gravity.START
            mapLibreMap.setStyle("https://api.baato.io/api/v1/styles/breeze_cdn?key=$BAATO_API_KEY") {
                Log.d("MapManager", "Map style loaded successfully")
                enableUserLocation()
                onMapReady(mapLibreMap)

            }
        }
    }

    private fun enableUserLocation() {
        appContext?.let {
            if (map == null) return

            if (checkLocationPermissions()) {
                val locationComponent = map!!.locationComponent
                locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(it, map!!.style!!)
                        .useDefaultLocationEngine(true)
                        //right now using default location engine
                        //TODO create own location engine
                        .locationEngineRequest(
                            LocationEngineRequest.Builder(750).setFastestInterval(750).setPriority(
                                LocationEngineRequest.PRIORITY_HIGH_ACCURACY
                            ).build()
                        ).build()
                )
                locationComponent.isLocationComponentEnabled = true
                locationComponent.cameraMode = CameraMode.TRACKING
                locationComponent.renderMode = RenderMode.COMPASS
            } else {
                requestLocationPermissions()
            }
        }
    }

    fun moveCamera(latLng: LatLng, zoom: Double) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(zoom).build()
        //handles the camera movement animation to the desired coordinates
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000)
    }

    private fun checkLocationPermissions(): Boolean {
        return appContext?.let {
            ActivityCompat.checkSelfPermission(
                it, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } ?: false
    }

    private fun requestLocationPermissions() {
        appContext?.let {
            ActivityCompat.requestPermissions(
                (it as Activity), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001
            )
        }
    }

    fun initializeLocationEngine() {
        appContext?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }
        requestLocationUpdates()
    }

    fun goToUserLocation() {
        appContext?.let {
            if (ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient?.getCurrentLocation(
                    com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null
                )?.addOnSuccessListener { location ->
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        moveCamera(latLng, 18.0)
                    } else {
                        Toast.makeText(
                            it, "Unable to get current location.", Toast.LENGTH_SHORT
                        ).show()
                    }
                }?.addOnFailureListener {
                    Toast.makeText(appContext, "Failed to get location.", Toast.LENGTH_SHORT).show()
                }
            } else {
                requestLocationPermissions()
            }
        }
    }

    fun getUserLocation(onLocationReceived: (LatLng) -> Unit) {
        appContext?.let {
            if (ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient?.getCurrentLocation(
                    com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null
                )?.addOnSuccessListener { location ->
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        onLocationReceived(latLng)
                    } else {
                        Toast.makeText(
                            it, "Unable to get current location.", Toast.LENGTH_SHORT
                        ).show()
                    }
                }?.addOnFailureListener {
                    Toast.makeText(appContext, "Failed to get location.", Toast.LENGTH_SHORT).show()
                }
            } else {
                requestLocationPermissions()
            }
        }
    }


    //if time I will implement my own location engine for location updates
    fun requestLocationUpdates() {
        if (checkLocationPermissions()) {
            try {
                val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
                    com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, 5000
                ).apply {
                    setMinUpdateIntervalMillis(2000)
                    setWaitForAccurateLocation(true)
                }.build()

                val locationCallback = object : com.google.android.gms.location.LocationCallback() {
                    override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                        locationResult.lastLocation?.let { location ->
                            val latLng = LatLng(location.latitude, location.longitude)
//                            moveCamera(latLng, 18.0)
                        }
                    }
                }

                fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null)
            } catch (e: SecurityException) {
                Log.e("MapManager", "Location permission denied: ${e.message}")
            }
        } else {
            requestLocationPermissions()
        }
    }

    fun resetCompass() {
        map?.animateCamera(CameraUpdateFactory.bearingTo(0.0))
    }


    fun addRedMarker(latLng: LatLng) {
        map?.let { mapLibreMap ->
            mapLibreMap.getStyle { style ->
                if (style.isFullyLoaded) { // this is used to ensure style is fully loaded before modifying
                    val sourceId = "marker-source"
                    val layerId = "marker-layer"
                    // removing existing source and layer if they exist
                    style.getLayer(layerId)?.let { style.removeLayer(it) }
                    style.getSource(sourceId)?.let { style.removeSource(it) }

                    //adding the red marker image after checking
                    if (style.getImage("red_marker_svg") == null) {
                        style.addImage(
                            "red_marker_svg", BitmapFactory.decodeResource(
                                appContext?.resources, R.drawable.red_marker
                            )
                        )
                    }

                    // Create a new marker feature
                    val point = Point.fromLngLat(latLng.longitude, latLng.latitude)
                    val feature = Feature.fromGeometry(point)
                    val featureCollection = FeatureCollection.fromFeatures(arrayOf(feature))

                    val source = GeoJsonSource(sourceId, featureCollection)
                    style.addSource(source)

                    val symbolLayer = SymbolLayer(layerId, sourceId).withProperties(
                        iconImage("red_marker_svg"),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconSize(1.0f)
                    )
                    style.addLayer(symbolLayer)

                    // Move camera
                    moveCamera(latLng, 15.0)
                }
            }
        }
    }

    fun fetchPlacesInCurrentViewport(type: String) {
        map?.let { mapLibreMap ->
            val visibleRegion = mapLibreMap.projection.visibleRegion
            val latLngBounds = visibleRegion.latLngBounds

            val southWest = latLngBounds.southWest
            val northEast = latLngBounds.northEast

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val places = getNearbyPlaces(
                        lat = (southWest.latitude + northEast.latitude) / 2,
                        lon = (southWest.longitude + northEast.longitude) / 2,
                        radius = calculateRadius(southWest, northEast),
                        type = type
                    )

                    places.data.forEach { place ->
                        addMarker(
                            LatLng(place.centroid.lat, place.centroid.lon), place
                        )
                        handleGeometry(place.geometry, mapLibreMap)
                    }
                } catch (e: Exception) {
                    Toast.makeText(appContext, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }

    fun addMarker(location: LatLng, place: NearbyPlace) {
        map?.let { mapLibreMap ->
            mapLibreMap.getStyle { style ->
                if (style.isFullyLoaded) {
                    Log.d(
                        "MapMarker",
                        "Adding marker for ${place.name} at ${location.latitude}, ${location.longitude}"
                    )

                    // ensure marker image is added once
                    if (style.getImage("red_marker_svg") == null) {
                        appContext?.resources?.let { res ->
                            val bitmap = BitmapFactory.decodeResource(res, R.drawable.red_marker)
                            if (bitmap != null) {
                                style.addImage("red_marker_svg", bitmap)
                                Log.d("MapMarker", "Marker image added successfully!")
                            } else {
                                Log.e("MapMarker", "Failed to load marker image!")
                            }
                        }
                    }

                    val sourceId = "source-${location.latitude}-${location.longitude}"
                    val layerId = "layer-${location.latitude}-${location.longitude}"

                    val point = JSONObject().apply {
                        put("type", "Point")
                        put("coordinates", JSONArray().apply {
                            put(location.longitude)
                            put(location.latitude)
                        })
                    }

                    val featureCollection = JSONObject().apply {
                        put("type", "FeatureCollection")
                        put("features", JSONArray().apply {
                            put(JSONObject().apply {
                                put("type", "Feature")
                                put("geometry", point)
                                put("properties", JSONObject().apply {
                                    put("title", place.name)
                                })
                            })
                        })
                    }

                    if (style.getSource(sourceId) == null) {
                        val source = GeoJsonSource(sourceId, featureCollection.toString())
                        style.addSource(source)
                        Log.d("MapMarker", "Source added: $sourceId")
                    }

                    if (style.getLayer(layerId) == null) {
                        val layer = SymbolLayer(layerId, sourceId).withProperties(
                            iconImage("red_marker_svg"),
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true),
                            iconSize(0.3f),

                            // Add text label (place name)
                            textSize(12f),
                            textOffset(arrayOf(0f, 2.0f)), // Adjust text above marker
                            textAllowOverlap(true),
                            textIgnorePlacement(true)

                        )

                        style.addLayer(layer)
                        Log.d("MapMarker", "Layer added: $layerId")
                    }
                    mapLibreMap.addOnMapClickListener { latLng ->
                        if (latLng.distanceTo(location) < 100) {  // Adjust threshold as needed
                            PlaceShowManager().setSelectedPlace(place)
                            Log.d("MapMarker", "Marker clicked: ${place.name}")
                            true
                        } else {
                            false
                        }
                    }

                }
            }
        }
    }

    fun clearMapFeatures() {
        map?.let { mapLibreMap ->
            mapLibreMap.getStyle()?.sources?.forEach { source ->
                if (source.id.startsWith("source-") || source.id.startsWith("polygon-source-")) {
                    mapLibreMap.getStyle()?.removeSource(source.id)
                }
            }
            mapLibreMap.getStyle()?.layers?.forEach { layer ->
                if (layer.id.startsWith("layer-") || layer.id.startsWith("polygon-layer-")) {
                    mapLibreMap.getStyle()?.removeLayer(layer.id)
                }
            }
        }
    }

    fun onDestroy() {
        try {
            mapView?.onDestroy()
        } catch (e: Exception) {
            Log.e("MapManager", "Error in onDestroy: ${e.localizedMessage}", e)
        }
    }

    fun onStart() {
        try {
            mapView?.onStart()
        } catch (e: Exception) {
            Log.e("MapManager", "Error in onStart: ${e.localizedMessage}", e)
        }
    }

    fun onStop() {
        try {
            mapView?.onStop()
        } catch (e: Exception) {
            Log.e("MapManager", "Error in onStop: ${e.localizedMessage}", e)
        }
    }

    fun onResume() {
        try {
            mapView?.onResume()
        } catch (e: Exception) {
            Log.e("MapManager", "Error in onResume: ${e.localizedMessage}", e)
        }
    }

    fun onPause() {
        try {
            mapView?.onPause()
        } catch (e: Exception) {
            Log.e("MapManager", "Error in onPause: ${e.localizedMessage}", e)
        }
    }

    fun onLowMemory() {
        try {
            mapView?.onLowMemory()
        } catch (e: Exception) {
            Log.e("MapManager", "Error in onLowMemory: ${e.localizedMessage}", e)
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        try {
            mapView?.onSaveInstanceState(outState)
        } catch (e: Exception) {
            Log.e("MapManager", "Error in onSaveInstanceState: ${e.localizedMessage}", e)
        }
    }
}