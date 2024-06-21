package com.example.ripcurrent.tool.pictureTool

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File

// 將 Bitmap 保存為 PNG 檔案
fun SaveBitmapAsPNG(bitmap: Bitmap, context: Context) {
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
        // 通知系統相冊有新圖片
        MediaScannerConnection.scanFile(context, arrayOf(uri.path), arrayOf(mimeType), null)
    }
}


