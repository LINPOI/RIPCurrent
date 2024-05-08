package com.example.ripcurrent.page

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.BackHandlerPress


@Composable
fun SettingPage(modifier: Modifier, navController: NavHostController) {
    Scaffold(

        topBar = {
            Row (modifier= Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.onBackground)
                .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                Text(text = stringResource(R.string.setting), fontSize = 26.sp)
            }

        }
    ){
        Column(
            modifier= Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            OutlinedButton(
                modifier = Modifier
                .fillMaxWidth()
                .size(80.dp)
                .padding(top = 10.dp), shape = MaterialTheme.shapes.extraSmall,
                onClick = {
                          navController.navigate(Screens.IntroductionToRipCurrentsPage.name)
                },
            )
            {
                Text(text = stringResource(R.string.what_is_rip_current), fontSize = 30.sp)
            }
        }
    }

    BackHandlerPress( ){
        Log.i("9453","返回")
        navController.navigate(Screens.OpenPage.name)
    }
}