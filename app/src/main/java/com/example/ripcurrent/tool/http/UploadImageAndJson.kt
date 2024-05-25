package com.example.ripcurrent.tool.http

import android.graphics.Bitmap
import android.util.Log
import com.example.ripcurrent.tool.BitmapToFIle
import com.example.ripcurrent.tool.Data.PhotoInfo
import com.example.ripcurrent.tool.http.Retrofit.apiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

suspend fun UploadImageAndJson(filename:String,image: Bitmap, jsonObject: PhotoInfo) {
    val imageFile= BitmapToFIle(image)
    // 創建圖片的 RequestBody
    val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    val filePart = MultipartBody.Part.createFormData(name = "image",filename = filename, requestFile)

    // 創建 JSON 的 RequestBody
    val json = Gson().toJson(jsonObject)
    val jsonRequestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

    // 上傳圖片和 JSON
    val response = apiService.uploadImage(filePart, jsonRequestBody)

    if (response.isSuccessful) {
        // 上傳成功
        Log.i("MyLog","Upload success: ${response.body()?.string()}")
    } else {
        // 上傳失敗
        Log.e("MyLog","Upload failed: ${response.errorBody()?.string()}")
    }
}