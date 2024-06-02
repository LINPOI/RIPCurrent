package com.example.ripcurrent.tool

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.example.ripcurrent.R
import java.lang.Math.toDegrees

@Composable
fun GetPosition(modifier: Modifier):String{

        val context = LocalContext.current
        var rotationAngle by remember { mutableStateOf(0f) }
    var towards=""
    var angle by remember { mutableStateOf(0f) }

        LaunchedEffect(key1 = context) {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
//        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
//        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//        val rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

            sensorManager.registerListener(object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

                override fun onSensorChanged(event: SensorEvent?) {

                    event?.let {
                        when (it.sensor?.type) {
                            Sensor.TYPE_MAGNETIC_FIELD -> {

                                val magneticValues = event.values
                                // Extract the magnetic field's x and y components.
                                val x = magneticValues[0]
                                val y = magneticValues[1]
                                val z = magneticValues[2]
                                // Calculate the rotation angle using atan2 function.
                                val horizontalAngle =
                                    toDegrees(kotlin.math.atan2(y.toDouble(), x.toDouble())).toFloat() * -1
                                val verticalAngle =
                                    toDegrees(kotlin.math.atan2(z.toDouble(), x.toDouble())).toFloat()
                                Log.i("linpoi","$horizontalAngle, $verticalAngle")
                                if(horizontalAngle+180>180){
                                    if(verticalAngle+180<180) {
                                        angle = horizontalAngle
                                    }else {
                                        angle = verticalAngle
                                    }
                                }else if(horizontalAngle+180<180){
                                    if(verticalAngle+180>180) {
                                        angle = verticalAngle
                                    }else {
                                        angle = horizontalAngle
                                    }
                                }
                                rotationAngle = angle
                            }
                            Sensor.TYPE_GYROSCOPE -> {
                                // Handle gyroscope data if needed
                            }
                            Sensor.TYPE_ACCELEROMETER -> {
                                // Handle accelerometer data if needed
                            }
                            Sensor.TYPE_ROTATION_VECTOR -> {
                                // Handle rotation vector data if needed
                            }
                        }
                    }
                }
            }, magnetometer, SensorManager.SENSOR_DELAY_GAME)
        }
//    Canvas(modifier = modifier.fillMaxSize()) {
//        // Draw compass needle
//        val centerX = size.width / 2
//        val centerY = size.height / 2
//        val needleLength = size.width / 3
//        val needleColor = Color.Red
//
//        rotate(rotationAngle) {
//            drawLine(
//                color = needleColor,
//                start = Offset(centerX, centerY),
//                end = Offset(centerX + needleLength, centerY),
//                strokeWidth = 8.dp.toPx()
//            )
//        }
//    }
    UdmImage(imageResource = R.drawable.compass,modifier=modifier.graphicsLayer(rotationZ = rotationAngle+90))
    var positionAngle = angle + 180
    if(positionAngle>337.5 || positionAngle<=22.5){
        towards = "東"
    }
    else if(positionAngle>22.5 && positionAngle<=67.5){
        towards = "東北"
    }
    else if(positionAngle>67.5 && positionAngle<=112.5){
        towards = "北"
    }
    else if(positionAngle>112.5 && positionAngle<=157.5){
        towards = "西北"
    }
    else if(positionAngle>157.5 && positionAngle<=202.5){
        towards = "西"
    }
    else if(positionAngle>202.5 && positionAngle<=247.5){
        towards = "西南"
    }
    else if(positionAngle>247.5 && positionAngle<=292.5){
        towards = "南"
    }
    else if(positionAngle>292.5 && positionAngle<=337.5){
        towards = "東南"
    }
    return towards
}