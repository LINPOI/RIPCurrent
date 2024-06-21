package com.example.ripcurrent.page

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.backHandler.BackHandlerPress
import com.example.ripcurrent.tool.pictureTool.CameraPreviewScreen

@Composable
fun CameraPage(modifier: Modifier, navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CameraPreviewScreen(navController)
    }
    BackHandlerPress( ){
        Log.i("9453","返回")
        navController.navigate(Screens.MainPage.name)
    }
}


