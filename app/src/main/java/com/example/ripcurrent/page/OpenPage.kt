package com.example.ripcurrent.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.DoubleBackHandler
import com.example.ripcurrent.tool.UdmImage
import com.example.ripcurrent.tool.UdmtextFields

@Composable
fun OpenPage(modifier: Modifier = Modifier, navController: NavHostController) {
    var urls by remember { mutableStateOf("") } // 創建一個可變的列表來存儲成功加載的 URL
    Scaffold (
        topBar = {
            Row(modifier= Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.onBackground)
                .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                val location= UdmtextFields(stringResource = R.string.nul, modifier = Modifier, selectFieldsStyle = 1, keyboardType = KeyboardType.Text){
                    //如同點選搜尋時執行指令
                }
                Icon( imageVector =  Icons.TwoTone.Search , contentDescription = null,
                    modifier
                        .padding(start = 10.dp)
                        .size(40.dp)
                        .clickable { })
            }

        },
        bottomBar= {
            Row (modifier= Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.onBackground),
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
            modifier
                .fillMaxSize()
                .padding(pad),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item    {
                Image(
                    painter = rememberAsyncImagePainter(model = urls, onError = {
                            Log.e("linpoi", it.result.throwable.message!!)
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp, 100.dp)
                )

            }
            item {
                Button(onClick = {
                    urls= "https://192.168.50.160/rip_current/test/get/check"
                }) {
                    Text(text = "重置")
                }
            }
        }

    }
    DoubleBackHandler( LocalContext.current)
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilImage() {
    // A variable to store the image URL
    var imageUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image( // The Image component to load the image with the Coil library
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier.size(500.dp, 400.dp)
        )

        Button(
            onClick = {
                imageUrl = "https://picsum.photos/200/300"
            }
        ) {
            Text("Load Image")
        }
    }
}