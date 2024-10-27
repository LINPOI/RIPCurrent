package com.example.ripcurrent.camera

import ImageClassifier
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream

// 將 ImageProxy 轉換為 Bitmap 並進行分類
@SuppressLint("UnsafeOptInUsageError")
fun processImageProxy(
    imageProxy: ImageProxy,
    imageClassifier: ImageClassifier,
    onResult: (Boolean) -> Unit
) {
    val bitmap = imageProxy.toBitmap()
    val targetFound = imageClassifier.classify(bitmap)

    onResult(targetFound)
    imageProxy.close() // 關閉以釋放資源
}

// 將 ImageProxy 轉為 Bitmap
fun ImageProxy.toBitmap(): Bitmap {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)
    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}