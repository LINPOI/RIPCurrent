package com.example.ripcurrent.tool.pictureTool

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ripcurrent.Data.PhotoInfoResponse
import com.example.ripcurrent.tool.http.Retrofit
import com.example.ripcurrent.tool.savedataclass.readDataClass
import kotlinx.coroutines.launch

class RipCurrentPhotoViewModel(context: Context) : ViewModel() {
    /*
    代號區
     */
    val FSM_ONLY_JPEG = 1
    val FSM_ONLY_JSON = 2
    val FSM_BOTH = 3
    /*

     */
    val photoInfo = mutableStateListOf<PhotoInfoResponse>()

    init {
        fetchRipCurrentData(context)
    }
    private fun fetchRipCurrentData(context: Context) {
        viewModelScope.launch {

        try{
            val sequence= readDataClass(context,"Sequence",3)
            val lat=readDataClass(context,"lat","lat").toFloatOrNull()?:0.0f
            val lng=readDataClass(context,"lng","lng").toFloatOrNull()?:0.0f
            val response = Retrofit.apiService.getImagesInfo(fsm = FSM_ONLY_JSON, fs = sequence, lat = lat.toString(), lon = lng.toString())
            val body = response.body()
            body?.forEach{
                photoInfo.add(PhotoInfoResponse(it.PhotoName,it.PhotoLocation, it.PhotoCoordinate_lng, it.PhotoCoordinate_lat, it.PhotoFilming_time, it.PhotoPosition,it.LikeQuantity,"", it.IsLike))
            }
        } catch (e: Exception) {
            Log.e("API Failure", e.message!!)
        }
        }
    }
}