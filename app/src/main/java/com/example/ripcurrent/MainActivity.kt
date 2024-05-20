package com.example.ripcurrent


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.ripcurrent.ui.theme.RipcurrentTheme

enum class Screens {
    SignInPage ,
    MainPage,
    CameraPage,
    SettingPage,
    IntroductionToRipCurrentsPage,
    EditPhotoPage;
}

class MainActivity : ComponentActivity() {

var isGranted = false
    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
//           if (isGranted) { } else { }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (PackageManager.PERMISSION_GRANTED) {ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) -> {}else -> { cameraPermissionRequest.launch(Manifest.permission.CAMERA) } }
        setContent {
            RipcurrentTheme {
                AppNavigator()
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RipcurrentTheme {
        AppNavigator()
    }
}