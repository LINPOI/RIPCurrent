package com.example.ripcurrent.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.DoubleBackHandler
import com.example.ripcurrent.tool.UdmImage
import com.example.ripcurrent.tool.UdmtextFields

@Composable
fun OpenPage(modifier: Modifier = Modifier, navController: NavHostController) {

    Scaffold (
        topBar = {
            Row(modifier= Modifier
                .fillMaxWidth().border(0.5.dp, MaterialTheme.colorScheme.onBackground).padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                val location= UdmtextFields(stringResource = R.string.nul, modifier = Modifier, selectFieldsStyle = 1, keyboardType = KeyboardType.Text){
                    //如同點選搜尋時執行指令
                }
                Icon( imageVector =  Icons.TwoTone.Search , contentDescription = null,modifier.padding(start = 10.dp).size(40.dp).clickable {  })
            }

        },
        bottomBar= {
            Row (modifier= Modifier
                .fillMaxWidth().border(0.5.dp, MaterialTheme.colorScheme.onBackground),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                UdmImage(imageResource = R.drawable.camera){
                    navController.navigate(Screens.CameraPage.name)
                }
                OutlinedButton(modifier= Modifier
                    .size(width = 230.dp, height = 30.dp)
                    .background(MaterialTheme.colorScheme.background),
                    onClick = { navController.navigate(Screens.OpenPage.name) }) {

                }
                UdmImage(imageResource = R.drawable.setting) {
                    navController.navigate(Screens.SettingPage.name)
                }

            }
        }
    ) { pad ->
        LazyColumn(
            modifier.padding(pad)
        ) {

        }

    }
    DoubleBackHandler( LocalContext.current)
}
