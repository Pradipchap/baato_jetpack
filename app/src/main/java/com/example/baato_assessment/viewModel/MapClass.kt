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
                onMapReady(mapLibreMap)
            }
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


    fun requestLocationUpdates() {
        // Check if location permissions are granted
        if (checkLocationPermissions()) {
            try {
                // Request location updates
                fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                    if (location != null) {
                        // Use the location
                        val latLng = LatLng(location.latitude, location.longitude)
                        moveCamera(latLng, 18.0)
                    } else {
                        Log.e("MapManager", "Last known location is null")
                        Toast.makeText(context, "Unable to get current location.", Toast.LENGTH_SHORT).show()
                    }
                }?.addOnFailureListener { exception ->
                    Log.e("MapManager", "Failed to get location: ${exception.message}")
                    Toast.makeText(context, "Failed to get location.", Toast.LENGTH_SHORT).show()
                }
            } catch (securityException: SecurityException) {
                // Handle the case where the permission is denied
                Log.e("MapManager", "SecurityException: ${securityException.message}")
                Toast.makeText(context, "Location permission denied.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Request location permissions if not granted
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