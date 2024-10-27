package com.example.ripcurrent.tool.pictureTool
//來源 https://github.com/DeepuGeorgeJacob/CmrXTutorial/tree/main
import ImageClassifier
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.ripcurrent.Screens
import com.example.ripcurrent.camera.processImageProxy
import com.example.ripcurrent.page.rotateBitmap
import com.example.ripcurrent.tool.currentTime.GetCurrentTime
import com.example.ripcurrent.tool.savedataclass.saveDataClass
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewScreen(navController: NavHostController) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val imageClassifier = ImageClassifier(context, "model.tflite")
    val activity = LocalContext.current as? ComponentActivity
    var hasCameraPermission by remember { mutableStateOf(false) }
    // Use Accompanist Permission API to handle permission requests
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val detected = remember { mutableStateOf(false) } // 檢測狀態
    LaunchedEffect(cameraPermissionState.status.isGranted) {
        hasCameraPermission = cameraPermissionState.status.isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (hasCameraPermission) {
        val preview = Preview.Builder().build()
        val previewView = remember { PreviewView(context) }
        val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val imageCapture = remember { ImageCapture.Builder().build() }
        // 即時影像分析
        val imageAnalyzer = ImageAnalysis.Builder()
            .setTargetResolution(android.util.Size(224, 224))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    processImageProxy(imageProxy, imageClassifier) { targetFound ->
                        detected.value = targetFound // 更新檢測狀態
                        imageProxy.close() // 記得關閉 imageProxy
                    }
                }
            }

        LaunchedEffect(lensFacing) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                } catch (e: Exception) {
                    Log.e("CameraPreviewScreen", "Camera initialization failed", e)
                }
            }, ContextCompat.getMainExecutor(context))
        }

        // 顯示相機畫面和即時標記框
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

            if (detected.value) {
                // 當檢測到目標時疊加標記框
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val paint = Paint().apply {
                        color = Color.Red // 確保顏色格式正確
                        strokeWidth = 8f
                        style = PaintingStyle.Stroke  // 注意這裡要使用 PaintingStyle 而不是 Paint.Style
                    }

                    // 計算框架位置和大小
                    val rectWidth = size.width * 0.4f
                    val rectHeight = size.height * 0.4f
                    val left = (size.width - rectWidth) / 2f
                    val top = (size.height - rectHeight) / 2f
                    drawRect(
                        color = Color.Red,
                        topLeft = Offset(left, top),
                        size = androidx.compose.ui.geometry.Size(rectWidth, rectHeight),
                        style = Stroke(width = 8f)  // 設置為只畫框
                    )
                }

            }
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()){
                OpenAlbumScreen(imageCapture, context, Modifier, navController)
            }
        }
    } else {
        // Permission denied or not yet granted
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            // Show a message or UI indicating that camera permission is required
            Text(text = "Camera permission is required", style = MaterialTheme.typography.titleSmall)
        }
    }
}

fun captureImage(imageCapture: ImageCapture, context: Context,navController: NavHostController,position: String) {
    val imageClassifier = ImageClassifier(context, "model.tflite")
    val name = "CameraxImage.jpeg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.i("linpoi","Successes")
                outputFileResults.savedUri?.let {
                    val inputStream =if(it.toString() != "") context.contentResolver.openInputStream(it) else null


                    inputStream?.use { stream ->

                        Log.i("0123", "新增圖片")
                        //解碼圖片
                        var bitmap = BitmapFactory.decodeStream(stream)
                        bitmap = rotateBitmap(bitmap, 90f)
                        /// 使用 ImageClassifier 進行辨識
                        val targetFound = imageClassifier.classify(bitmap)

                        // 根據分類結果顯示訊息
                        if (targetFound) {
                            Log.i("ImageClassifier", "目標已發現")
                            bitmap=imageClassifier.classifyAndMark(bitmap)
                        } else {
                            Log.i("ImageClassifier", "未發現目標")
                        }
                        //丟回圖片變數
                        saveDataClass(context,"updatePicture",bitmap)
                        //保存圖片
                    }
                    saveDataClass(context,"ImageUrl", it.toString())
                    saveDataClass(context,"currentTime", GetCurrentTime())
                    saveDataClass(context,"Position",position)
                    Log.i("linpoi","CameraX,$it")
                    navController.navigate(Screens.EditPhotoPage.name)
                }


            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("linpoi","Failed $exception")
                navController.navigate(Screens.EditPhotoPage.name)
            }

        })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }