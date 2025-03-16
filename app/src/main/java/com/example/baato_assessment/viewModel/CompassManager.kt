package com.example.baato_assessment.viewModel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.roundToInt

class CompassManager(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private var onRotationChanged: ((Float) -> Unit)? = null

    fun startListening(onRotationChanged: (Float) -> Unit) {
        this.onRotationChanged = onRotationChanged
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            val orientationAngles = FloatArray(3)

            // Convert sensor values into a rotation matrix
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            // Extract azimuth (rotation around Z-axis)
            val azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()

            // Round to prevent excessive UI updates
            val roundedAzimuth = azimuth.roundToInt().toFloat()

            // Invert direction to match screen rotation
            onRotationChanged?.invoke(-roundedAzimuth)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed, but required by the interface
    }
}
