package com.example.ripcurrent.page

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import changeGmailAddress
import com.example.ripcurrent.Data.Member
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.backHandler.BackHandlerPress
import com.example.ripcurrent.tool.Check.ExtraSpaces
import com.example.ripcurrent.tool.Check.Length
import com.example.ripcurrent.tool.Check.NullData
import com.example.ripcurrent.tool.custmozed.UdmtextFields
import com.example.ripcurrent.tool.http.Retrofit
import com.example.ripcurrent.tool.savedataclass.readDataClass
import com.example.ripcurrent.tool.savedataclass.saveDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginPage(modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val sourceNav = readDataClass(context, "SourceNav",Screens.SettingPage.name)
    var member by remember { mutableStateOf(Member()) }
    var wrong by remember { mutableIntStateOf(R.string.nul) }
    var alert by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier
    ) {
        Column(
            modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            member.MemberGmail =
                UdmtextFields(stringResource = R.string.gmail, imeAction = ImeAction.Next)

            UdmtextFields(stringResource = R.string.password, passwordTextField = true) {
                member.MemberGmail = ExtraSpaces(member.MemberGmail)
                member.MemberPassword = ExtraSpaces(it)
                Log.i("linpoi", "${member.MemberGmail},$it")
                if (!NullData(
                        member.MemberGmail,
                        member.MemberPassword
                    ) && Length(member.MemberPassword, member.MemberGmail)
                ) {
                    //輸入正確
                    member.MemberGmail= changeGmailAddress(member.MemberGmail).toString()
                    Log.i("linpoi", "${member.MemberGmail},${it}${sourceNav}成功")
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            if(Retrofit.apiService.loginMember(member).isSuccessful){
                                //登入成功
                                Log.i("linpoi", "${member.MemberGmail},${it},${sourceNav}登入成功")
                                navController.navigate(sourceNav)
                                saveDataClass(context,"Member",member)
                            }else if (Retrofit.apiService.loginMember(member).code()==401){
                                wrong =R.string.wrong_password
                            }else{
                                alert=true
                            }

                        }catch (e:Exception){
                            Log.e("linpoi", e.toString())
                        }

                        }
                } else {
                    //輸入錯誤
                    wrong =Wrong(member.MemberGmail,member.MemberPassword)
                }
            }
            Text(text = stringResource(id = wrong), color = Color.Red)
            if(alert){
                alert=alert(member,navController,sourceNav)
            }
        }

    }
    BackHandlerPress( ){
        Log.i("9453","返回")
        navController.navigate(sourceNav)
    }
}
fun Wrong(s1:String,s2:String):Int{
    return if (NullData(s1)) {
        R.string.gmail_cannot_be_empty
    } else if (NullData(s2)) {
        R.string.password_cannot_be_empty
    } else if (!Length(s1) ){
        R.string.gmail_number_must_be_at_least_6_characters
    } else if (!Length(s2) ){
        R.string.password_must_be_at_least_6_characters
    } else {
        R.string.gmail_or_password_is_incorrect
    }
}
@Composable
fun alert(member: Member = Member(), navController: NavHostController, ReSourseNav:String):Boolean{
    val context = LocalContext.current
    var save by remember { mutableStateOf(-1) }
    AlertDialog(
        text = { Text(text = stringResource(R.string.click_ok_to_register)) },
        onDismissRequest = { },
        confirmButton = {
            TextButton(onClick = { save=1 }) {
                Text(stringResource(R.string.ok))

            }
            },
        dismissButton = {
            TextButton(onClick = { save=0}) {
                Text(stringResource(R.string.cancel))
            }
        })
    if(save==1){
        LaunchedEffect(Unit) {
            // 在這裡調用suspend函數\
            member.MemberGmail= changeGmailAddress(member.MemberGmail).toString()
            Retrofit.apiService.insertMember(member)
            saveDataClass(context,"Member",member)
            navController.navigate(ReSourseNav)
        }
    }
    return save != 0
}