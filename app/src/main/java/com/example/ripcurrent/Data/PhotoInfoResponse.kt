package com.example.ripcurrent.Data
data class PhotoInfoResponse(
    var PhotoName:String="",
    var PhotoLocation: String = "",
    var PhotoCoordinate_lng: String = "",
    var PhotoCoordinate_lat: String = "",
    var PhotoFilming_time: String = "",
    var PhotoPosition: String = "",
    var LikeQuantity:Int=0,
    var LocationCode:String="",
    var IsLike:Boolean= false,
)