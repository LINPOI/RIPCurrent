package com.example.ripcurrent


import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ripcurrent.ui.theme.RipcurrentTheme
import com.google.android.gms.location.FusedLocationProviderClient

lateinit var fusedLocationProviderClient: FusedLocationProviderClient

class MainActivity : ComponentActivity() {
    private val REQUEST_NOTIFICATION_PERMISSION = 1


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, you can show notifications now
            } else {
                // Permission denied, show some explanation to the user
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RipcurrentTheme {
               AppNavigator()
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RipcurrentTheme {
       AppNavigator()
    }
}