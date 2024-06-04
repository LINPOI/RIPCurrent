package com.example.ripcurrent.tool

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ripcurrent.Data.PhotoInfoResponse
import com.example.ripcurrent.tool.http.Retrofit
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {

    val photoInfo = mutableStateListOf<PhotoInfoResponse>()

    init {
        loadPhotoData()
    }

    private fun loadPhotoData() {
        viewModelScope.launch {
            try {
                val response = Retrofit.apiService.getImagesInfo()
                Log.i("linpoi", response.toString())
                val body = response.body()
                body?.forEach {
                    photoInfo.add(PhotoInfoResponse(it.PhotoName,it.PhotoLocation, it.PhotoCoordinate_lng, it.PhotoCoordinate_lat, it.PhotoFilming_time, it.PhotoPosition))
                }
            } catch (e: Exception) {
                Log.e("linpoi", e.message!!)
            }
        }
    }
}
