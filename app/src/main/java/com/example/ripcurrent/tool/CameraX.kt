package com.example.ripcurrent.tool
//來源 https://github.com/DeepuGeorgeJacob/CmrXTutorial/tree/main
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.ripcurrent.Screens
import com.example.ripcurrent.page.rotateBitmap
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
    val activity = LocalContext.current as? ComponentActivity
    var hasCameraPermission by remember { mutableStateOf(false) }
    // Use Accompanist Permission API to handle permission requests
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

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

        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
            OpenAlbumScreen(imageCapture, context, Modifier, navController)
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
                Log.i("linpoi","Failed $exception")
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