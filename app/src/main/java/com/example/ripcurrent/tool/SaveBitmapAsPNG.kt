package com.example.ripcurrent.tool

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.ripcurrent.tool.Data.Member
import com.example.ripcurrent.tool.http.Retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File

// 將 Bitmap 保存為 PNG 檔案
fun SaveBitmapAsPNG(bitmap: Bitmap, context: Context, member: Member) {
    // 定義要保存的圖片的相關信息
    val mimeType = "image/jpeg"
    val relativeLocation = Environment.DIRECTORY_PICTURES + File.separator + "ripcurrentX-Image"

    // 創建 ContentValues 對象，用於描述要插入的文件的屬性
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "ripcurrent.jpeg")
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
        }
    }

    // 使用 ContentResolver 插入圖片並獲取其 URI
    val resolver = context.contentResolver
    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    // 如果插入成功，將 Bitmap 寫入 OutputStream
    imageUri?.let { uri ->
        resolver.openOutputStream(uri)?.use { persenOutputstream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, persenOutputstream)
            // 關閉OutputStream

        }
/*

  將圖片變為bitmap，再轉換為ByteArray，最後變為filePart

*/
        // 伺服器圖片格式編輯
        val serverOutputstream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, serverOutputstream)
        //獲得jpeg格式圖片
        val jpegData: ByteArray = serverOutputstream.toByteArray()
        // 創建一個臨時文件，並將 JPEG 數據寫入其中
        val tempFile = createTempFile()
        tempFile.writeBytes(jpegData)

        // 將臨時文件轉換為請求體的一部分
        val filePart = MultipartBody.Part.createFormData(
            name = "image", // 檔案欄位名稱，與伺服器端匹配
            filename = "${member.MemberGmail}.jpg", // 檔案名稱，可以自定義 需地點使用者日期
            body = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull()) // 檔案內容類型
        )

    // 發起網路請求
        CoroutineScope(Dispatchers.Main).launch {

            try {
                // 發送Http請求
                val response: retrofit2.Response<ByteArray> = Retrofit.apiService.uploadImage(filePart)

                // 檢查回應
                if (response.isSuccessful) {

                    // 接收回應
                    val jpeg: ByteArray? = response.body()

                    // 解析回應
                }else {

                    Log.e("linpoi","錯誤${response.errorBody()}")
                }

            } catch (e: Exception) {
                // 发生异常，处理异常信息
                // ...
                Log.e("linpoi", "異常$e")
            }
        }
        // 通知系統相冊有新圖片
        MediaScannerConnection.scanFile(context, arrayOf(uri.path), arrayOf(mimeType), null)
    }
}


