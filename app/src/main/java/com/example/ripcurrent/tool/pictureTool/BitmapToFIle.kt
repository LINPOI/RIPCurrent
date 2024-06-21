package com.example.ripcurrent.tool.pictureTool

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File

fun BitmapToFIle(bitmap: Bitmap): File {
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
    return tempFile
}