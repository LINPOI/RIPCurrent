package com.example.ripcurrent.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ripcurrent.Data.Member
import com.example.ripcurrent.Data.PhotoInfo
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.backHandler.BackHandlerPress
import com.example.ripcurrent.tool.currentTime.GetCurrentTime
import com.example.ripcurrent.tool.custmozed.UdmtextFields
import com.example.ripcurrent.tool.http.UploadImageAndJson
import com.example.ripcurrent.tool.savedataclass.readDataClass
import com.example.ripcurrent.tool.savedataclass.readDataClass_Bitmap
import getCoordinate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPhotoInformationPage(modifier: Modifier, navController: NavHostController) {
    val context=LocalContext.current
    var member by remember {
        mutableStateOf(readDataClass(context,"Member") ?: Member())
    }
    val bitmap= readDataClass_Bitmap(context,"bitmap")
    val currentTime = readDataClass(context,"currentTime") ?: GetCurrentTime()
    val position= readDataClass(context,"Position","")
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current

   val coordinate= getCoordinate()
    Scaffold (
        topBar = {
            Row (modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center){
                Text(text = stringResource(R.string.picture_information), fontSize = 25.sp)
            }
        },
        bottomBar = {

        }
    ){pad->

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // 設置點擊監聽器，清除焦點
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .padding(pad)
        ){
            item {
                Card (modifier = Modifier.padding(10.dp)){
                    Column (modifier = Modifier.padding(10.dp),verticalArrangement = Arrangement.Center){
                        if(bitmap!=null) Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.size(300.dp))

                        UdmtextFields(R.string.user, showContent = member.MemberName, imeAction = ImeAction.Next){
                            if(it!=member.MemberName)member.MemberName=it
                        }
                        Text(text = stringResource(R.string.location)+":${coordinate.address}",
                            modifier =  Modifier.pointerInput(Unit){
                            detectTapGestures(onLongPress = {
                                clipboardManager.setText(AnnotatedString(coordinate.address))
                            })
                        })
                        Text(text = stringResource(R.string.coordinate)+":${coordinate.lng},${coordinate.lat}",
                            modifier =  Modifier.pointerInput(Unit){
                                detectTapGestures(onLongPress = {
                                    clipboardManager.setText(AnnotatedString("${coordinate.lng},${coordinate.lat}"))
                                })
                            })
                        Text(text = stringResource(R.string.filming_time)+":"+currentTime.timeFormatter1)
                        Text(text = stringResource(R.string.direction)+":$position")
                        Row (modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp), verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.End){
                            Icon(Icons.Filled.Send, contentDescription = null,modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    if (bitmap != null) {

                                        //SaveBitmapAsPNG(bitmap, context)  //儲存於本機
                                        val filename=coordinate.address+member.MemberName+currentTime.timeFormatter2
                                        val info= PhotoInfo(PhotoLocation = coordinate.address, PhotoCoordinate_lat = coordinate.lat.toString(), PhotoCoordinate_lng = coordinate.lng.toString(), PhotoFilming_time = currentTime.timeFormatter2, PhotoPosition = position)
                                        //Log.i("MyLog","$info")
                                        CoroutineScope(Dispatchers.Main).launch {
                                            UploadImageAndJson(filename,bitmap,info)
                                        }
                                        navController.navigate(Screens.MainPage.name)
                                    } else {
                                        Log.i("MyLog", "沒有圖片")
                                    }
                                })

                        }
                    }
                }
            }
        }
    }
    BackHandlerPress( ){
        Log.i("9453","返回")
        navController.navigate(Screens.CameraPage.name)
    }
}


