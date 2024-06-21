package com.example.ripcurrent.page

import Coordinate
import android.Manifest
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ripcurrent.Data.LikePhoto
import com.example.ripcurrent.Data.Member
import com.example.ripcurrent.Data.PhotoInfoResponse
import com.example.ripcurrent.Data.UserReq
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.LocaltionSevice.LocationService
import com.example.ripcurrent.tool.backHandler.DoubleBackHandler
import com.example.ripcurrent.tool.currentTime.formatDateTime
import com.example.ripcurrent.tool.custmozed.UdmImage
import com.example.ripcurrent.tool.custmozed.UdmtextFields
import com.example.ripcurrent.tool.hint.backGroundHint.ShowNotification
import com.example.ripcurrent.tool.http.Retrofit
import com.example.ripcurrent.tool.http.URL
import com.example.ripcurrent.tool.pictureTool.PhotoViewModel
import com.example.ripcurrent.tool.savedataclass.readDataClass
import com.example.ripcurrent.tool.savedataclass.saveDataClass
import com.example.ripcurrent.tool.zipTool.listFilesInDirectory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import getAddressFromCoordinates
import getCoordinate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

const val TIMESORT=1
const val LIKESORT=2
const val DISTANCESORT=3
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainPage(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val member = readDataClass(context, "Member") ?: Member()
    val locationService = remember { LocationService(context) }
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    //紀錄滾動
    val listState = rememberLazyListState() // 紀錄 LazyColumn 的滾動狀態
    var update by remember {
        mutableStateOf(false)
    }
    //聚焦
    val focusManager = LocalFocusManager.current
    //位置紀錄
    var coordinate by remember {
        mutableStateOf(Coordinate())
    }
    val sequence by remember {
        mutableIntStateOf(readDataClass(context, "Sequence", DISTANCESORT))
    }
    //給予要求
    var userReq by remember {
        mutableStateOf( UserReq(UserGmail = member.MemberGmail, Sequence = sequence, UserLongitude = coordinate.lng.toFloat(), UserLatitude =coordinate.lat.toFloat()))
    }
    //圖片資訊
    var photoInfo = PhotoViewModel(context, userReq =userReq ).photoInfo
    coordinate = getCoordinate()
    var buttonBackColor by remember {
        mutableStateOf(false)
    }
    // UpdateNearToFar(photoInfo, coordinate)
    var distance by remember {
        mutableDoubleStateOf(0.0)
    }
    var distanceHint by remember {
        mutableDoubleStateOf(0.0)
    }
    var openSort by remember {
        mutableStateOf(false)
    }
    var alertState by remember {
        mutableStateOf(readDataClass(context, "AlertState", false))
    }
    distanceHint=readDataClass(context,"DistanceHint",100f).toDouble()
   // Log.i("linpoi", "distance:$distance,distanceHint:$distanceHint")
    LaunchedEffect(Unit) {
        delay(1000)
        // 更新 coordinate
        if (locationPermissionState.status.isGranted) {
            locationService.getLastLocation()
            locationService.locationFlow.collect { location ->
                location?.let {
                 //   Log.e("linpoi", "location:$it")
                    coordinate = getAddressFromCoordinates(context, it.first, it.second)
                }
                // 更新距離
                distance = Distance(photoInfo, coordinate)
            //    Log.e("linpoi", "distance:$distance,distanceHint:$distanceHint")
                if (distance <= distanceHint && !alertState) {
                    Log.e("linpoi", "ShowNotification")
                    while(true){
                        ShowNotification(context)
                        delay(180000)
                    }
                }
                delay(600000)
                alertState=false
                saveDataClass(context,"AlertState",true)
            }


        }
    }
    Scaffold(
        topBar = {
            Column(
                modifier
                    .border(0.5.dp, MaterialTheme.colorScheme.onBackground)
                    .fillMaxWidth(),verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {
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
                        photoInfo=Search(it, photoInfo)
                        Log.i("linpoi", "photoInfo:$photoInfo")
                    }
                    Icon(imageVector = Icons.TwoTone.Search, contentDescription = null,
                        modifier
                            .padding(start = 10.dp)
                            .size(40.dp)
                            .clickable {
                                //與1.相同
                                photoInfo=Search(location, photoInfo)
                                focusManager.clearFocus()
                            })
                }
//                NearToFarButton(photoInfo, coordinate,buttonBackColor){
//                    buttonBackColor = true
//                }
                Row (modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center){
                    if(openSort){
                        OutlinedButton(onClick = {
                            userReq.Sequence=TIMESORT
                            photoInfo = PhotoViewModel(context,userReq).photoInfo
                            saveDataClass(context, "Sequence", TIMESORT)

                            navController.navigate(Screens.MainPage.name)
                        }) {
                            Text(text = "時間優先")
                        }
                        OutlinedButton(onClick = {
                            userReq.Sequence= LIKESORT
                            photoInfo = PhotoViewModel(context,userReq).photoInfo
                            saveDataClass(context, "Sequence", LIKESORT)

                            navController.navigate(Screens.MainPage.name)
                        }) {
                            Text(text = "按讚優先")
                        }
                        OutlinedButton(onClick = {
                            userReq.Sequence= DISTANCESORT
                            photoInfo = PhotoViewModel(context,userReq).photoInfo
                            saveDataClass(context, "Sequence", DISTANCESORT)

                            navController.navigate(Screens.MainPage.name)
                        }) {
                            Text(text = "距離優先")
                        }
                    }
                }
                Icon(imageVector = if(openSort)Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown, contentDescription = null,Modifier.clickable { openSort = !openSort })
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
            CoroutineScope(Dispatchers.Main).launch {
                listState.scrollToItem(readDataClass(context, "Item", 0))
            }
            itemsIndexed(photoInfo) { index, it ->
                RipCurrentInfo(
                    member,
                    it,
                    index,
                    navController,
                    userReq ,
                    imageClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            navController.navigate(Screens.ShowImagePage.name)
                            saveDataClass(context, "Item", index)
                        }
                    },
                    likeClick = {
                        // photoInfo = viewModel.photoInfo
//                        CoroutineScope(Dispatchers.Main).launch {
//                            photoInfo=PhotoViewModel(context).photoInfo
//                            navController.navigate(Screens.MainPage.name)
//                            saveDataClass(context, "Item", index)
//                        }

                    }
                )
            }
            item {
//                Text(text = "$distance")
//                Text(text = "$coordinate")
//                Button(onClick = {photoInfo=PhotoViewModel(context).photoInfo}) {
//                    Text(text = "d")
//                }
                ReStartButton(context) {
                    photoInfo = PhotoViewModel(context,userReq).photoInfo

                    CoroutineScope(Dispatchers.Main).launch {
                        // NearToFar(photoInfo,coordinate)
                        distanceHint = readDataClass(context, "DistanceHint", 100f).toDouble()
                        navController.navigate(Screens.MainPage.name)
                        saveDataClass(context, "Item", 0)
                        update = true

                    }


                }
                if (update) {
                    coordinate = getCoordinate()
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
fun RipCurrentInfo(
    member: Member,
    photoInfo: PhotoInfoResponse,
    index: Int,
    navController: NavHostController,
    userReq: UserReq,
    imageClick: () -> Unit = {},
    likeClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val url = "${URL}photo/get/one/"
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
                modifier = Modifier
                    .size(160.dp, 280.dp)
                    .clickable {
                        imageClick()
                        saveDataClass(context, "ShowImage", url + photoInfo.PhotoName)

                    }
            )
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    val isLike=photoInfo.IsLike
                    var like by remember {
                        mutableStateOf(photoInfo.IsLike)
                    }
                    var likeQuantity by remember {
                        mutableIntStateOf(photoInfo.LikeQuantity)
                    }

                    Icon(if (!like) Icons.Filled.FavoriteBorder else Icons.Filled.Favorite,
                        contentDescription = null,
                        Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        try {
                                            // 更新
                                            val updatedQuantity = updateLikeStatus(like, photoInfo, member, userReq)
                                            likeQuantity = updatedQuantity.body()!![index].LikeQuantity
                                            like = updatedQuantity.body()!![index].IsLike
                                            likeClick()
                                            Log.i("linpoi", "likeclick:$photoInfo")
                                        } catch (e: Exception) {
                                            Log.e("linpoi", "Error updating like status", e)
                                        }
                                    }
                                }
                            )
                        })

                    Text(text = "$likeQuantity")
                }
                Title(
                    text = stringResource(id = R.string.filming_time),
                    Modifier.background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                )
                Text(text = time, Modifier.padding(top = 10.dp, bottom = 10.dp))

                Title(
                    text = stringResource(id = R.string.direction),
                    Modifier.background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                )
                Text(
                    text = photoInfo.PhotoPosition,
                    Modifier.padding(top = 10.dp, bottom = 10.dp),
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                )

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

/*
由進至遠選擇按鈕(已刪除)
// */
//@Composable
//fun NearToFarButton(
//    photoInfo: MutableList<PhotoInfoResponse>,
//    coordinate: Coordinate,
//    buttonBackColor: Boolean,
//    TODO: () -> Unit = {}
//) {
//    OutlinedButton(
//        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (buttonBackColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background),
//        onClick = {
//            NearToFar(photoInfo, coordinate)
//            TODO()
//        }) {
//        Text(
//            text = stringResource(R.string.near_to_far),
//            color = if (buttonBackColor) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
//        )
//    }
//}

/*
處理搜尋功能
 */
fun Search(string: String, list: List<PhotoInfoResponse>): SnapshotStateList<PhotoInfoResponse> {
    val filteredList = list.filter { item ->
        item.PhotoName.contains(string)
    }
    Log.i("linpoi", "filteredList:$filteredList")
    return filteredList.toMutableStateList()
}




fun Distance(photoInfo: MutableList<PhotoInfoResponse>, coordinate: Coordinate): Double {
    try {
//        val lat1 = 23.575355351988794
//        val lng1 = 119.58252274222751
//        val lat =
//            if (photoInfo.isNotEmpty()) photoInfo[0].PhotoCoordinate_lat.toDoubleOrNull() else -1.0
//        val lng =
//            if (photoInfo.isNotEmpty()) photoInfo[0].PhotoCoordinate_lng.toDoubleOrNull() else -1.0
        val myLat = coordinate.lat
        val myLng = coordinate.lng
        var distance = Double.MAX_VALUE
        photoInfo.forEach{
            var lat =it.PhotoCoordinate_lat.toDoubleOrNull()
            var lng = it.PhotoCoordinate_lng.toDoubleOrNull()
            lat = lat ?: -1.0
            lng = lng ?: -1.0
                val value=calculateDistance(lat, lng, myLat, myLng)
            if(value<distance){
                distance=value
            }
        }


        Log.i("linpoi", "Distance:${distance}")
        return distance
    } catch (e: Exception) {
        Log.e("linpoi", "Distance:${e}")
    }
    return -1.0
}

fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
    val earthRadius = 6372.8 // Radius of the Earth in kilometers
    Log.e("linpoi", "$lat1,$lng1, $lat2,$lng2")
    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(lng2 - lng1)
    val originLat = Math.toRadians(lat1);
    val destinationLat = Math.toRadians(lat2);
    val a =
        sin(dLat / 2).pow(2.toDouble()) + sin(dLng / 2).pow(2.toDouble()) * cos(originLat) * cos(
            destinationLat
        );
    val c = 2 * asin(sqrt(a));
    return earthRadius * c * 1000 // Distance in meters
}

suspend fun updateLikeStatus(isLiked: Boolean, photoInfo: PhotoInfoResponse, member: Member,userReq: UserReq):Response<List<PhotoInfoResponse>> {
    if (!isLiked) {
        Retrofit.apiService.likePhoto(
            LikePhoto(
                member.MemberGmail,
                photoInfo.PhotoName
            )
        )
    } else {
        Retrofit.apiService.delikePhoto(
            LikePhoto(
                member.MemberGmail,
                photoInfo.PhotoName
            )
        )
    }
    return Retrofit.apiService.getImagesInfo(userReq)
}