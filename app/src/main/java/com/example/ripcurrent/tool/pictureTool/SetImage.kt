package com.example.ripcurrent.tool.pictureTool

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

/*
    initialPicture 使用ID =R.drawable
    filename 使用ImageName()的字串
    照片形狀看要不要圓形(true)
    照片大小需不需要修正
 */
@Composable
fun SetImage(
    initialPicture: Int?,
    fileName: String,
    circleShape: Boolean = true, size: Int = 120,
    width: Int = size,
    height: Int = size,
    //noPicture: (Boolean) -> Unit = {},//這樣預設就可以不填東西也行
) {
    //容納圖片的變數
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    val context = LocalContext.current

    //獲取選擇的圖片
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // 使用者選擇了圖片後的處理邏輯
        uri?.let { imageUri ->
            //不是空的
            try {
                //打開圖片
                val inputStream = context.contentResolver.openInputStream(imageUri)
                inputStream?.use { stream ->

                    Log.i("0123", "新增圖片")
                    //解碼圖片
                    val bitmap = BitmapFactory.decodeStream(stream)
                    //丟回圖片變數
                    selectedImage = bitmap?.asImageBitmap()
                    //保存圖片
 //                   saveImageToInternalStorage(context, member, bitmap, fileName)
                }
            } catch (e: Exception) {
                Log.e("0123", "Error: ${e.message}")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = width.dp, height = height.dp)
                .clickable {
                    // 啟動圖片選擇器
                    launcher.launch("image/*")
                }
                .clip(if (circleShape) CircleShape else RoundedCornerShape(1.dp)),

            contentAlignment = Alignment.Center
        ) {
            // 顯示大頭照
            selectedImage?.let {
                Image(
                    bitmap = it,
                    contentDescription = "User Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(if (circleShape) CircleShape else RoundedCornerShape(1.dp)),
                    contentScale = ContentScale.FillWidth
                )
            } ?: run {
                // 如果沒有選擇照片，顯示預設的圖
                ShowSavedImage(initialPicture, fileName, circleShape, size, width, height)

            }
        }

    }
}

//fun saveImageToInternalStorage(context: Context,bitmap: Bitmap, fileName: String) {
//    val directory: File = context.filesDir
//    val file = File(directory, fileName)
//
//    FileOutputStream(file).use { outputStream ->
//        val byteArray = bitmapToByteArray(bitmap)
//        outputStream.write(byteArray)
//    }
//
//    // 上傳二進制數據到服務器
//    val byteArray = bitmapToByteArray(bitmap)
//    //上傳
//    Log.i("0123","傳入資料庫$byteArray")
//    Log.i("0123","圖片傳送成功:$fileName")
//
//    post(mapOf(SetUrl(member).account,SetUrl(member).house_id),byteArray,SetUrl().postimageUrl())//傳入資料庫
//}
fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

fun loadImageFromInternalStorage(context: Context, fileName: String): Bitmap? {
    val directory: File = context.filesDir
    val file = File(directory, fileName)
    return BitmapFactory.decodeFile(file.absolutePath)
}

@Composable
fun ShowSavedImage(
    initialPicture: Int?,
    fileName: String,
    circleshape: Boolean,
    size: Int = 120,
    width: Int = size,
    height: Int = size,
) {//R.drawable,filename 初始照片、用戶儲存後更新照片
    val context = LocalContext.current
    // 從內部存儲中讀取已保存的圖像
    var savedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(Unit) {
        // 在 IO 線程中讀取圖像
        val loadedBitmap: Bitmap? = withContext(Dispatchers.IO) {
            loadImageFromInternalStorage(context, fileName = fileName)
        }

        // 切換回主線程更新 UI
        withContext(Dispatchers.Main) {
            loadedBitmap?.let {
                // 如果成功讀取圖像，則更新 UI
                savedBitmap = it
            }
        }
    }


    savedBitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Saved Image",
            modifier = Modifier
                .size(width = width.dp, height = height.dp)
                .clip(if (circleshape) CircleShape else RoundedCornerShape(1.dp)),
            contentScale = ContentScale.FillWidth
        )
    } ?: run {
        // 如果未找到圖像
        if (initialPicture != null) {
            Image(
                painter = painterResource(id = initialPicture),
                contentDescription = "Default User Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(if (circleshape) CircleShape else RoundedCornerShape(1.dp)),
                contentScale = ContentScale.FillWidth
            )
        }else{
            Column(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .background(Color.White)
                    .border(1.5.dp, MaterialTheme.colorScheme.onBackground)
                    .size(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Icon(
                    Icons.Default.Add, contentDescription = "Add"
                )
            }
        }
    }
}

