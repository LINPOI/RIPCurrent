package com.example.ripcurrent.page

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material.icons.twotone.Settings
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
import androidx.compose.ui.graphics.Color
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
import com.example.ripcurrent.Data.Coordinate
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
import com.example.ripcurrent.tool.http.SELECTURL
import com.example.ripcurrent.tool.pictureTool.RipCurrentPhotoViewModel
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

const val TIMESORT = 1
const val LIKESORT = 2
const val DISTANCESORT = 3

//修改開始
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
    var coordinate = readDataClass(context, "Coordinate") ?: Coordinate()
    Log.i("linpoi", "初始值coordinate:$coordinate")
    val sequence = readDataClass(context, "Sequence", 4)

    //圖片資訊
    var allPhotoInfo by remember {
        mutableStateOf(SnapshotStateList<PhotoInfoResponse>())
    }
    var photoInfo by remember {
        mutableStateOf(SnapshotStateList<PhotoInfoResponse>())
    }
    val newCoordinate = getCoordinate()
    Log.e("linpoi", "newCoordinate:$newCoordinate")
    //更新位置
    if ((coordinate.lat == 0.0 || coordinate.lng == 0.0) && ((newCoordinate.lat != 0.0 || newCoordinate.lng != 0.0) && (newCoordinate.lat != coordinate.lat || newCoordinate.lng != coordinate.lng))) {
        val lng = newCoordinate.lng
        val lat = newCoordinate.lat
        val address = newCoordinate.address
        saveDataClass(context, "lng", lng.toString())
        saveDataClass(context, "lat", lat.toString())
        saveDataClass(context, "address", address)
    }
    // UpdateNearToFar(photoInfo, coordinate)
    var distance by remember {
        mutableDoubleStateOf(0.0)
    }
    var distanceHintRange by remember {
        mutableDoubleStateOf(0.0)
    }
    var distanceHint by remember {
        mutableStateOf(false)
    }
    var openSort by remember {
        mutableStateOf(false)
    }
    var alertState by remember {
        mutableStateOf(readDataClass(context, "AlertState", false))
    }
//    var locations by remember {
//        mutableStateOf(mutableListOf<String>())
//    }
    distanceHintRange = readDataClass(context, "DistanceHint", 100f).toDouble()
    // Log.i("linpoi", "distance:$distance,distanceHint:$distanceHint")
    //給予要求
    val userReq = readDataClass(context, "userReq") ?: UserReq()
    var recentLocation = ""
    Log.i("UserReq", "userReq:${userReq}")
    LaunchedEffect(Unit) {
        allPhotoInfo = RipCurrentPhotoViewModel(context).photoInfo
        photoInfo = allPhotoInfo
        // 更新 coordinate
        if (locationPermissionState.status.isGranted) {
            locationService.getLastLocation()
            locationService.locationFlow.collect { location ->
                location?.let {
                    //   Log.e("linpoi", "location:$it")
                    coordinate = getAddressFromCoordinates(context, it.first, it.second)
                }
            }
        }
        delay(10000)
    }
    // 更新距離
    distance = Distance(allPhotoInfo, context) {
        recentLocation = it
    }

    LaunchedEffect(distanceHintRange, distance) {
        Log.e(
            "測試中",
            "distance:$distance,distanceHint:$distanceHintRange,alertState:$alertState"
        )
        if (distanceHintRange >= distance) {
            distanceHint = true
        } else {
            distanceHint = false
        }
        val con = distanceHint && alertState
        Log.e("linpoi", con.toString())
        if (con) {
            Log.e("linpoi", "ShowNotification")
            while (true) {
                ShowNotification(context, recentLocation)
                delay(180000)
            }

        }
        delay(600000)
        alertState = true
        saveDataClass(context, "AlertState", true)
    }
    Scaffold(
        topBar = {
            Column(
                modifier
                    .border(0.5.dp, MaterialTheme.colorScheme.onBackground)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    //設定
                    Icon(imageVector = Icons.TwoTone.Settings, contentDescription = null, modifier
                        .size(40.dp)
                        .clickable {
                            navController.navigate(Screens.SettingPage.name)
                            focusManager.clearFocus()
                        })
                    //搜尋文字框
                    val searchLocation = UdmtextFields(
                        stringResource = R.string.nul,
                        modifier = Modifier,
                        selectFieldsStyle = 1,
                        keyboardType = KeyboardType.Text
                    ) {
                        //1.如同點選搜尋時執行指令
                        photoInfo = Search(it, allPhotoInfo)
                        Log.i("linpoi", "photoInfo:$photoInfo")
                    }
                    //搜尋按鈕
                    Icon(imageVector = Icons.TwoTone.Search, contentDescription = null,
                        modifier
                            .size(40.dp)
                            .clickable {
                                //與1.相同
                                photoInfo = Search(searchLocation, allPhotoInfo)
                                focusManager.clearFocus()
                            })
                }
//                NearToFarButton(photoInfo, coordinate,buttonBackColor){
//                    buttonBackColor = true
//                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (openSort) {
                        OutlinedButton(onClick = {
                            saveDataClass(context, "Sequence", TIMESORT)
                            photoInfo = RipCurrentPhotoViewModel(context).photoInfo
                            navController.navigate(Screens.MainPage.name)
                        }) {
                            Text(text = stringResource(R.string.time_priority))
                        }
                        OutlinedButton(onClick = {
                            saveDataClass(context, "Sequence", LIKESORT)
                            photoInfo = RipCurrentPhotoViewModel(context).photoInfo
                            navController.navigate(Screens.MainPage.name)
                        }) {
                            Text(text = stringResource(R.string.like_first))
                        }
                        OutlinedButton(onClick = {
                            saveDataClass(context, "Sequence", DISTANCESORT)
                            photoInfo = RipCurrentPhotoViewModel(context).photoInfo
                            navController.navigate(Screens.MainPage.name)
                        }) {
                            Text(text = stringResource(R.string.distance_first))
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //重置
                    Icon(Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                            .wrapContentWidth(Alignment.Start)
                            .clickable {
                                CoroutineScope(Dispatchers.Main).launch {
                                    // NearToFar(photoInfo,coordinate)
                                    distanceHintRange =
                                        readDataClass(context, "DistanceHint", 100f).toDouble()
                                    navController.navigate(Screens.MainPage.name)
                                    saveDataClass(context, "Item", 0)
                                    update = true

                                }
                            })
                    //選單
                    Icon(
                        imageVector = if (openSort) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                            .clickable { openSort = !openSort }
                    )
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
            CoroutineScope(Dispatchers.Main).launch {
                listState.scrollToItem(readDataClass(context, "Item", 0))
            }

            itemsIndexed(photoInfo) { index, photo ->

//                if(!locations.contains(photo.PhotoLocation)){
//                    Row (modifier = Modifier.padding(10.dp).fillMaxWidth().background(MaterialTheme.colorScheme.onPrimary), horizontalArrangement = Arrangement.Center){
//                        Text(photo.PhotoLocation.substring(5), fontSize = 25.sp)
//                    }
//                    locations.add(photo.PhotoLocation)
//                }
                RipCurrentInfo(
                    member,
                    photo,
                    index,
                    navController,
                    userReq,
                    distanceHintRange,
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

        }
    }
    if (update) {
        coordinate = getCoordinate()
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
    distanceHintRange: Double,
    imageClick: () -> Unit = {},
    likeClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val hint = distanceHintRange >= calculateDistance(
        photoInfo.PhotoCoordinate_lat.toDouble(),
        photoInfo.PhotoCoordinate_lng.toDouble(),
        context
    )
    Color(255, 0, 0, 136)
    val url = "${SELECTURL}photo/get/one/"
    val title =
        if (photoInfo.PhotoLocation.length > 8) photoInfo.PhotoLocation.substring(8) else photoInfo.PhotoLocation
    val time = formatDateTime(photoInfo.PhotoFilming_time)
    Column(
        Modifier
            .padding(2.dp)
            .background(
                if (hint) Color(255, 0, 0, 136) else MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
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
                    Log.e("rememberAsyncImagePainter", it.result.throwable.message!!)
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
                                            val updatedQuantity =
                                                updateLikeStatus(like, photoInfo, member, userReq)
                                            likeQuantity =
                                                updatedQuantity.body()!![index].LikeQuantity
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
                if (hint) {
                    Text(
                        stringResource(R.string.attention_this_rip_current_is_within_the_warning_range),
                        color = Color(255, 245, 98, 255),
                        fontSize = 20.sp
                    )
                }


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
處理搜尋功能
 */
fun Search(string: String, list: List<PhotoInfoResponse>): SnapshotStateList<PhotoInfoResponse> {
    val filteredList = list.filter { item ->
        item.PhotoLocation.contains(string)
    }
    Log.i("linpoi", "filteredList:$filteredList")
    return filteredList.toMutableStateList()
}


fun Distance(
    photoInfo: MutableList<PhotoInfoResponse>,
    context: Context,
    location: (String) -> Unit = {}
): Double {
    try {
        var minLocation = ""

//        Log.e("測試中","myLat:$myLat,myLng:$myLng")
        var distance = Double.MAX_VALUE
        photoInfo.forEach {
            var lat = it.PhotoCoordinate_lat.toDoubleOrNull()
            var lng = it.PhotoCoordinate_lng.toDoubleOrNull()
            lat = lat ?: -1.0
            lng = lng ?: -1.0
            val value = calculateDistance(lat, lng, context)
            if (value < distance) {
                minLocation = it.PhotoLocation
                distance = value
            }
        }
        Log.i("測試中", "Distance:${distance}")
        location(if (minLocation.length > 8) minLocation.substring(8) else minLocation)
        return distance
    } catch (e: Exception) {
        Log.e("linpoi", "Distance:${e}")
    }
    return -1.0
}

fun calculateDistance(lat1: Double, lng1: Double, context: Context): Double {
    val myLat = readDataClass(context, "lat", "lat").toDoubleOrNull() ?: 0.0
    val myLng = readDataClass(context, "lng", "lng").toDoubleOrNull() ?: 0.0
    val earthRadius = 6372.8 // Radius of the Earth in kilometers
    Log.e("linpoi", "$lat1,$lng1, $myLat,$myLng")
    val dLat = Math.toRadians(myLat - lat1)
    val dLng = Math.toRadians(myLng - lng1)
    val originLat = Math.toRadians(lat1)
    val destinationLat = Math.toRadians(myLat)
    val a =
        sin(dLat / 2).pow(2.toDouble()) + sin(dLng / 2).pow(2.toDouble()) * cos(originLat) * cos(
            destinationLat
        )
    val c = 2 * asin(sqrt(a));
    return earthRadius * c * 1000 // Distance in meters
}

suspend fun updateLikeStatus(
    isLiked: Boolean,
    photoInfo: PhotoInfoResponse,
    member: Member,
    userReq: UserReq
): Response<List<PhotoInfoResponse>> {
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