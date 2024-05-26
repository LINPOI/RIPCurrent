package com.example.ripcurrent.page

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.BackHandlerPress
import com.example.ripcurrent.tool.Data.Member
import com.example.ripcurrent.tool.UdmtextFields
import com.example.ripcurrent.tool.http.Retrofit
import com.example.ripcurrent.tool.readDataClass
import com.example.ripcurrent.tool.saveDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SettingPage(modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val member= readDataClass(context,"Member")?: Member()
    var changeNameisClicked by remember {
        mutableStateOf(false)
    }
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
                .padding(it)
                .pointerInput(Unit) {
                    // 設置點擊監聽器，清除焦點
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //使用者顯示設定
            Row {
                Text(text = stringResource(R.string.user) +"：")
                if(member.MemberGmail==""){
                    //如果沒註冊，則註冊
                    saveDataClass(context, "SourceNav", Screens.SettingPage.name)
                    Text(text = stringResource(R.string.not_logged_in), color = Color.Blue, textDecoration = TextDecoration.Underline,modifier = Modifier.clickable { navController.navigate(Screens.LoginPage.name) })
                }else if(member.MemberName==""&&!changeNameisClicked){
                    //如果尚未命名
                    Text(text = stringResource(R.string.click_to_give_name), color = Color.Gray, textDecoration = TextDecoration.Underline,modifier = Modifier.clickable { changeNameisClicked=true })
                }else if(changeNameisClicked){
                    //如果已命名
                    member.MemberName= UdmtextFields( keyboardType = KeyboardType.Text, modifier = Modifier.padding(end = 10.dp).height(60.dp)){ name->
                        member.MemberName=name
                        CoroutineScope(Dispatchers.Main).launch {
                            Retrofit.apiService.updateMember(member)
                            saveDataClass(context,"Member",member)
                            changeNameisClicked=false
                        }
                    }
                    //返回設定
                    BackHandlerPress( ){
                        Log.i("9453","返回")
                        navController.navigate(Screens.SettingPage.name)
                    }
                } else{
                    Text(text = member.MemberName,modifier = Modifier.clickable { changeNameisClicked=true})
                }
            }

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
        navController.navigate(Screens.MainPage.name)
    }
}