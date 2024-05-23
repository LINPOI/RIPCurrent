package com.example.ripcurrent


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ripcurrent.ui.theme.RipcurrentTheme
import com.google.android.gms.location.FusedLocationProviderClient

lateinit var fusedLocationProviderClient: FusedLocationProviderClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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