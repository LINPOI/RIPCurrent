package com.example.ripcurrent


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ripcurrent.page.CameraPage
import com.example.ripcurrent.page.CoilImage
import com.example.ripcurrent.ui.theme.RipcurrentTheme
import com.example.ripcurrent.page.EditPhotoPage
import com.example.ripcurrent.page.IntroductionToRipCurrentsPage

import com.example.ripcurrent.page.OpenPage
import com.example.ripcurrent.page.SettingPage

enum class Screens {
    OpenPage,
    CameraPage,
    SettingPage,
    IntroductionToRipCurrentsPage,
    EditPhotoPage
}

class MainActivity : ComponentActivity() {

var isGranted = false
    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
            if (isGranted) {

            } else {
                // Camera permission denied
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) -> {

            }
            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }

        }

        setContent {
            RipcurrentTheme {
                // A surface container using the 'background' color from the theme
                //GlideImage()
                AppNavigator()
                //CoilImage()
            }
        }
    }
}

@Composable
fun AppNavigator(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController() //管理應用程式中的導航流程
) {
    //   val context = LocalContext.current //獲取現在的context

    val backStackEntry by navController.currentBackStackEntryAsState() //導航堆疊入口
    // 初始畫面位置
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.OpenPage.name//當前路徑，route回傳名稱

    )

    Surface (modifier=Modifier.fillMaxSize())
    {
        //NavHost導航容器，負責顯示不同目的地之間的轉換
        NavHost(
            navController, startDestination = currentScreen.name,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            //切換至MainPage
            composable(Screens.OpenPage.name) {
                OpenPage(modifier, navController)
            }
            //切換至SecondPage
            composable(Screens.CameraPage.name) {
                CameraPage(modifier, navController)
            }
            //切換至ThirdPage
            composable(Screens.SettingPage.name) {
                SettingPage(modifier, navController)
            }
            composable(Screens.IntroductionToRipCurrentsPage.name) {
                IntroductionToRipCurrentsPage(modifier, navController)
            }
            composable(Screens.EditPhotoPage.name) {
                EditPhotoPage(modifier, navController)
                //CoilImage()
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