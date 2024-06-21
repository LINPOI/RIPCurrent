package com.example.ripcurrent.tool.pictureTool

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ripcurrent.Data.Member
import com.example.ripcurrent.Data.PhotoInfoResponse
import com.example.ripcurrent.Data.UserReq
import com.example.ripcurrent.tool.http.Retrofit
import com.example.ripcurrent.tool.savedataclass.readDataClass
import kotlinx.coroutines.launch

class PhotoViewModel(context: Context,userReq:UserReq)  : ViewModel() {

    val photoInfo = mutableStateListOf<PhotoInfoResponse>()

    init {
        loadPhotoData(context,userReq)
    }

    private fun loadPhotoData(context: Context,userReq:UserReq) {
        viewModelScope.launch {
            try {
                val  member= readDataClass(context,"Member") ?: Member()
                val response = Retrofit.apiService.getImagesInfo(userReq)
                Log.i("linpoi", response.toString()+"UserReq="+userReq)
                val body = response.body()
                body?.forEach {
                    photoInfo.add(PhotoInfoResponse(it.PhotoName,it.PhotoLocation, it.PhotoCoordinate_lng, it.PhotoCoordinate_lat, it.PhotoFilming_time, it.PhotoPosition,it.LikeQuantity,it.IsLike))
                }
            } catch (e: Exception) {
                Log.e("linpoi", e.message!!)
            }
        }
    }
}
