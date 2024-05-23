package com.example.ripcurrent.tool

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class CameraPermissionHelper(private val activity: ComponentActivity) {

    fun checkAndRequestCameraPermission(
        onPermissionGranted: () -> Unit={},
        onPermissionDenied: () -> Unit={}
    ) {
        val cameraPermissionRequest = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) -> {
                onPermissionGranted()
            }
            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }
}