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
import com.example.ripcurrent.tool.Data.Member
import com.example.ripcurrent.tool.UdmtextFields

@Composable
fun OpenPage(modifier: Modifier, navController: NavHostController) {
    val member = remember { mutableStateOf(Member()) }
    var wrong by remember { mutableIntStateOf(R.string.nul) }
    Surface(
        modifier = modifier
    ) {
        Column(
            modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            member.value.account =
                UdmtextFields(stringResource = R.string.account, imeAction = ImeAction.Next)

            UdmtextFields(stringResource = R.string.password, passwordTextField = true) {
                member.value.account = ExtraSpaces(member.value.account)
                member.value.password = ExtraSpaces(it)
                Log.i("linpoi", "${member.value.account},$it")
                if (!NullData(
                        member.value.account,
                        member.value.password
                    ) && Length(member.value.password, member.value.account)
                ) {
                    //登入成功
                } else {
                    //登入失敗
                    wrong =Wrong(member.value.account,member.value.password)
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