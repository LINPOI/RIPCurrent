package com.example.ripcurrent.page

import Coordinate
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.Data.PhotoInfoResponse
import com.example.ripcurrent.tool.DoubleBackHandler
import com.example.ripcurrent.tool.PhotoViewModel
import com.example.ripcurrent.tool.UdmImage
import com.example.ripcurrent.tool.UdmtextFields
import com.example.ripcurrent.tool.formatDateTime
import com.example.ripcurrent.tool.listFilesInDirectory
import com.example.ripcurrent.tool.saveDataClass
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import getCoordinate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainPage(modifier: Modifier = Modifier, navController: NavHostController) {
    val viewModel: PhotoViewModel = viewModel()
    val context = LocalContext.current
    //圖片資訊
    var photoInfo by remember { mutableStateOf(viewModel.photoInfo) }
    //紀錄滾動
    val listState = rememberLazyListState() // 紀錄 LazyColumn 的滾動狀態
    //聚焦
    val focusManager = LocalFocusManager.current
    //位置紀錄
    val coordinate = getCoordinate()
    var buttonBackColor by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            Column (modifier.border(0.5.dp, MaterialTheme.colorScheme.onBackground)){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val location = UdmtextFields(
                        stringResource = R.string.nul,
                        modifier = Modifier,
                        selectFieldsStyle = 1,
                        keyboardType = KeyboardType.Text
                    ) {
                        //1.如同點選搜尋時執行指令
                        Search(it,photoInfo)
                    }
                    Icon(imageVector = Icons.TwoTone.Search, contentDescription = null,
                        modifier
                            .padding(start = 10.dp)
                            .size(40.dp)
                            .clickable {
                                //與1.相同
                                Search(location,photoInfo)
                                focusManager.clearFocus()
                            })
                }
                NearToFarButton(photoInfo, coordinate,buttonBackColor){
                    buttonBackColor = true
                }
            }


        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, MaterialTheme.colorScheme.onBackground),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                UdmImage(imageResource = R.drawable.camera) {
                    navController.navigate(Screens.CameraPage.name)
                }
                OutlinedButton(modifier = Modifier
                    .size(width = 230.dp, height = 30.dp)
                    .background(MaterialTheme.colorScheme.background),
                    onClick = { navController.navigate(Screens.MainPage.name) }) {

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
                .pointerInput(Unit) {
                    // 設置點擊監聽器，清除焦點
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .padding(pad),
            state = listState,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(photoInfo) {
                RipCurrentInfo(it,navController)
            }
            item {
                ReStartButton(context) {
                    photoInfo = viewModel.photoInfo
                    CoroutineScope(Dispatchers.Main).launch {
                        listState.scrollToItem(index = 0)
                    }


                }
            }
        }

    }
    DoubleBackHandler(LocalContext.current)
}

// 計算兩個座標之間的距離
fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0].toDouble()
}


@Composable
fun RipCurrentInfo(photoInfo: PhotoInfoResponse,navController: NavHostController ) {
    val context = LocalContext.current
    val url = "http://192.168.50.160/rip_current/photo/get/one/"
    val title =
        if (photoInfo.PhotoLocation.length > 8) photoInfo.PhotoLocation.substring(8) else photoInfo.PhotoLocation
    val time = formatDateTime(photoInfo.PhotoFilming_time)
    Column(
        Modifier
            .padding(2.dp)
            .border(
                1.5.dp,
                MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.large
            )
    ) {
        Title(title)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 4.dp)
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = url + photoInfo.PhotoName, onError = {
                    Log.e("linpoi", it.result.throwable.message!!)
                }
                ),
                contentDescription = null,
                modifier = Modifier.size(160.dp, 280.dp).clickable {
                    saveDataClass(context,"ShowImage",url + photoInfo.PhotoName)
                    navController.navigate(Screens.ShowImagePage.name)
                }
            )
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(
                    text = stringResource(id = R.string.filming_time),
                    Modifier.background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                )
                Text(text = time)
            }
        }
    }
    Spacer(modifier = Modifier.size(4.dp))

}

@Composable
fun ReStartButton(context: Context, doIn: () -> Unit) {
    OutlinedButton(onClick = {
        //downloadAndSaveZipFile(context)
        Log.i("linpoi", listFilesInDirectory(context).toString())
        doIn()

    }, modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.reset))
    }
}

@Composable
fun Title(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.large),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onPrimary, fontSize = 25.sp)
    }
}
@Composable
fun NearToFarButton(
    photoInfo: MutableList<PhotoInfoResponse>,
    coordinate: Coordinate,
    buttonBackColor: Boolean,
    TODO: () -> Unit = {}
){
    OutlinedButton(colors = ButtonDefaults.outlinedButtonColors(containerColor = if (buttonBackColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background), onClick = {
        CoroutineScope(Dispatchers.Main).launch  {
            try {
                // 對 photoInfo 進行排序並更新列表
                Log.i("linpoi", photoInfo.toString())
                val sortedList = photoInfo.sortedBy { photo ->
                    val lat = photo.PhotoCoordinate_lat.toDoubleOrNull()
                    val lng = photo.PhotoCoordinate_lng.toDoubleOrNull()
                    if (lat != null && lng != null) {
                        Log.i("linpoi", "交換")
                        getDistance(coordinate.lat, coordinate.lng, lat, lng)

                    } else {
                        Double.MAX_VALUE
                    }

                }
                // 使用 mutableStateListOf 替換內容
                photoInfo.clear()
                photoInfo.addAll(sortedList)


            } catch (e: Exception) {
                Log.e("linpoi", e.message ?: "Unknown error")
            }

        }
        TODO()
    }) {
        Text(text = stringResource(R.string.near_to_far),color = if (buttonBackColor) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
    }
}
fun Search(string: String,list: List<PhotoInfoResponse>){
    val filteredList = list.filter { item ->
        item.PhotoName.contains(string)
    }
}