package com.example.ripcurrent.tool.LocaltionSevice

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationService(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val _locationFlow = MutableStateFlow<Pair<Double, Double>?>(null)
    val locationFlow: StateFlow<Pair<Double, Double>?> = _locationFlow

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        val locationTask: Task<Location> = fusedLocationClient.lastLocation
        locationTask.addOnSuccessListener { location ->
            location?.let {
                _locationFlow.value = Pair(it.latitude, it.longitude)
            }
        }
    }
}
