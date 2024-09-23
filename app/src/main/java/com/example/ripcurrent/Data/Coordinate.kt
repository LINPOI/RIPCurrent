package com.example.ripcurrent.Data

data class Coordinate(
    var lng: Double = 0.0,
    var lat: Double = 0.0,
    var address: String = ""
){
      fun convertToSaveCoordinate(): SaveCoordinate {
        return SaveCoordinate(lng.toString(), lat.toString(), address)
    }
}
