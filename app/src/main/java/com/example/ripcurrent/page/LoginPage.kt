package com.example.ripcurrent.page

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ripcurrent.R
import com.example.ripcurrent.tool.Check.ExtraSpaces
import com.example.ripcurrent.tool.Check.Length
import com.example.ripcurrent.tool.Check.NullData
import com.example.ripcurrent.tool.Check.changeGmailAddress
import com.example.ripcurrent.tool.Data.Member
import com.example.ripcurrent.tool.Data.Screens
import com.example.ripcurrent.tool.UdmtextFields
import com.example.ripcurrent.tool.http.Retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignInPage(modifier: Modifier, navController: NavHostController) {
    var member by remember { mutableStateOf(Member()) }
    var wrong by remember { mutableIntStateOf(R.string.nul) }
    Surface(
        modifier = modifier
    ) {
        Column(
            modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            member.MemberGmail =
                UdmtextFields(stringResource = R.string.account, imeAction = ImeAction.Next)

            UdmtextFields(stringResource = R.string.password, passwordTextField = true) {
                member.MemberGmail = ExtraSpaces(member.MemberGmail)
                member.MemberPassword = ExtraSpaces(it)
                Log.i("linpoi", "${member.MemberGmail},$it")
                if (!NullData(
                        member.MemberGmail,
                        member.MemberPassword
                    ) && Length(member.MemberPassword, member.MemberGmail)
                ) {
                    //登入成功

                    member.MemberGmail= changeGmailAddress(member.MemberGmail).toString()
                    Log.i("linpoi", "${member.MemberGmail},${it}成功")
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            Retrofit.apiService.insertMember(member)
                            navController.navigate(Screens.MainPage.name)
                        }catch (e:Exception){
                            Log.i("linpoi", e.toString())
                        }

                        }
                } else {
                    //登入失敗
                    wrong =Wrong(member.MemberGmail,member.MemberPassword)
                }
            }
            Text(text = stringResource(id = wrong), color = Color.Red)
        }

    }
}
fun Wrong(s1:String,s2:String):Int{
    return if (NullData(s1)) {
        R.string.account_cannot_be_empty
    } else if (NullData(s2)) {
        R.string.password_cannot_be_empty
    } else if (!Length(s1) ){
        R.string.account_number_must_be_at_least_6_characters
    } else if (!Length(s2) ){
        R.string.password_must_be_at_least_6_characters
    } else {
        R.string.account_or_password_is_incorrect
    }
}