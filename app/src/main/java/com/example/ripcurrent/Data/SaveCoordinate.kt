package com.example.ripcurrent.Data

data class SaveCoordinate(
    var lng: String = "",
    var lat: String = "",
    var address: String = ""
){
    fun convertToCoordinate(): Coordinate {
        val newLng=lng.toDoubleOrNull()?:0.0
        val newLat=lat.toDoubleOrNull()?:0.0
        return Coordinate(newLng, newLat, address)
    }
}
