package com.example.ripcurrent.tool.position

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import com.example.ripcurrent.tool.custmozed.UdmImage
import java.lang.Math.toDegrees
import kotlin.math.abs
import kotlin.math.atan2

@Composable
fun GetPosition(modifier: Modifier):String{

        val context = LocalContext.current
        var rotationAngle by remember { mutableStateOf(0f) }
    var towards=""
    var angle by remember { mutableStateOf(0f) }
    var isHorizontal by remember { mutableStateOf(false) }
    var isVerticalX by remember { mutableStateOf(false) }
    var positionAngle by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = context) {
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    when (it.sensor?.type) {
                        Sensor.TYPE_MAGNETIC_FIELD -> {
                            val magneticValues = it.values
                            val x = magneticValues[0]
                            val y = magneticValues[1]
                            val z = magneticValues[2]

                            // Calculate the rotation angle based on device orientation
                            angle = if (isHorizontal) {
                                toDegrees(atan2(y.toDouble(), x.toDouble())).toFloat() * -1
                            } else {
                                if(isVerticalX){
                                    toDegrees(atan2(y.toDouble(), z.toDouble())).toFloat() +180
                                }else{
                                    toDegrees(atan2(z.toDouble(), x.toDouble())).toFloat()
                                }
                            }

                            rotationAngle = angle
                            positionAngle =  if(isVerticalX) angle+90 else angle +180
                        }
                        Sensor.TYPE_ACCELEROMETER -> {
                            val accelerationValues = it.values
                            val ax = accelerationValues[0]
                            val ay = accelerationValues[1]
                            val az = accelerationValues[2]
                            // Check if the device is horizontal or vertical
                            isHorizontal = abs(az) > abs(ax) && abs(az) > abs(ay)
                            isVerticalX = abs(ax) > abs(ay) && abs(ax) > abs(az)
                        }
                    }
                }
            }
        }

        sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }
    UdmImage(imageResource = R.drawable.compass,modifier=modifier.graphicsLayer(rotationZ = rotationAngle+90))

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