package com.example.ripcurrent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ripcurrent.page.CameraPage
import com.example.ripcurrent.page.EditPhotoInformationPage
import com.example.ripcurrent.page.EditPhotoPage
import com.example.ripcurrent.page.IntroductionToRipCurrentsPage
import com.example.ripcurrent.page.LoginPage
import com.example.ripcurrent.page.MainPage
import com.example.ripcurrent.page.SearchPage
import com.example.ripcurrent.page.SettingPage
import com.example.ripcurrent.page.ShowImagePage

@Composable
fun AppNavigator(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController() //管理應用程式中的導航流程
) {
    //   val context = LocalContext.current //獲取現在的context

    val backStackEntry by navController.currentBackStackEntryAsState() //導航堆疊入口
    // 初始畫面位置
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.MainPage.name//當前路徑，route回傳名稱

    )

    Surface (modifier= Modifier.fillMaxSize())
    {
        //NavHost導航容器，負責顯示不同目的地之間的轉換
        NavHost(
            navController, startDestination = currentScreen.name,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            //切換至MainPage
            composable(Screens.EditPhotoInformationPage.name) {
                EditPhotoInformationPage(modifier, navController)
            }
            composable(Screens.LoginPage.name) {
                LoginPage(modifier, navController)
            }
            composable(Screens.MainPage.name) {
                MainPage(modifier, navController)
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
            composable(Screens.ShowImagePage.name) {
                ShowImagePage(modifier, navController)
            }
            composable(Screens.SearchPage.name) {
                SearchPage(modifier, navController)
            }
        }
    }
}