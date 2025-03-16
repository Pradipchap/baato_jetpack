package com.example.baato_assessment.viewModel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
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

//use this class to manage map from anywhere in the app
class MapManager(private val context: Context) {

    private var mapView: MapView? = null
    private var map: MapLibreMap? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null


    fun initializeMap(mapView: MapView, onMapReady: (MapLibreMap) -> Unit) {
        this.mapView = mapView
        mapView.getMapAsync { mapLibreMap ->
            this.map = mapLibreMap
            mapLibreMap.setStyle("https://api.baato.io/api/v1/styles/breeze_cdn?key=bpk.YRfF8dHCw5QDEJUD3mOy-I3SdH52xqiD-BMG0iq3FgAZ") {
                Log.d("MapManager", "Map style loaded successfully")
                enableUserLocation()
                onMapReady(mapLibreMap)
            }
        }
    }

    private fun enableUserLocation() {
        if (map == null) return

        if (checkLocationPermissions()) {
            val locationComponent = map!!.locationComponent
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(context, map!!.style!!)
                    .useDefaultLocationEngine(true)
                    //right now using default location engine
                    //TODO create own location engine
                    .locationEngineRequest(LocationEngineRequest.Builder(750).setFastestInterval(750).setPriority(
                        LocationEngineRequest.PRIORITY_HIGH_ACCURACY).build())
                    .build()
            )
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS
        } else {
            requestLocationPermissions()
        }
    }

    fun moveCamera(latLng: LatLng, zoom: Double) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(zoom)
            .build()
        //handles the camera movement animation to the desired coordinates
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000)
    }

    private fun checkLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            (context as Activity),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1001
        )
    }
    fun initializeLocationEngine() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        requestLocationUpdates()
    }

    fun goToUserLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient?.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                ?.addOnSuccessListener { location ->
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        moveCamera(latLng, 18.0)
                    } else {
                        Toast.makeText(context, "Unable to get current location.", Toast.LENGTH_SHORT).show()
                    }
                }?.addOnFailureListener {
                    Toast.makeText(context, "Failed to get location.", Toast.LENGTH_SHORT).show()
                }
        } else {
            requestLocationPermissions()
        }
    }

    //if time I will implement my own location engine for location updates
    fun requestLocationUpdates() {
        if (checkLocationPermissions()) {
            try {
                val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
                    com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                    5000
                ).apply {
                    setMinUpdateIntervalMillis(2000)
                    setWaitForAccurateLocation(true)
                }.build()

                val locationCallback = object : com.google.android.gms.location.LocationCallback() {
                    override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                        locationResult.lastLocation?.let { location ->
                            val latLng = LatLng(location.latitude, location.longitude)
                            moveCamera(latLng, 18.0)
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