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
import com.example.ripcurrent.tool.savedataclass.saveDataClass
import kotlinx.coroutines.launch

class PhotoViewModel(context: Context)  : ViewModel() {

    val photoInfo = mutableStateListOf<PhotoInfoResponse>()

    init {
        loadPhotoData(context)
    }

    private fun loadPhotoData(context: Context) {
        viewModelScope.launch {
            try {
                val  member= readDataClass(context,"Member") ?: Member()
                val  sequence= readDataClass(context,"Sequence",4)

                val lat=readDataClass(context,"lat","lat").toFloatOrNull()?:0.0f
                val lng=readDataClass(context,"lng","lng").toFloatOrNull()?:0.0f
                val address=readDataClass(context,"address","address")
//                Log.e("測試中", "PhotoViewModelcoordinate:${Coordinate(lng.toDouble(),lat.toDouble(),address)}")
                val sendUserReq=readDataClass(context,"sendUserReq")?: UserReq(
                    UserGmail = member.MemberGmail,
                    Sequence = sequence,
                    UserLatitude=lat,
                    UserLongitude = lng
                )
                val response = Retrofit.apiService.getImagesInfo(sendUserReq)
//                Log.i("測試中", response.toString()+"UserReq="+sendUserReq+"seq:${readDataClass(context,"Sequence",4)}")
                if((sendUserReq.UserLatitude!=0.0f&&sendUserReq.UserLongitude!=0.0f)){
                    saveDataClass(context, "sendUserReq", sendUserReq)
                }
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
