package com.example.ripcurrent.page

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.ripcurrent.MainActivity
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.HandleBackPress
import com.example.ripcurrent.tool.readDataClass
import com.example.ripcurrent.tool.saveBitmapAsPNG
import io.getstream.sketchbook.Sketchbook
import io.getstream.sketchbook.rememberSketchbookController

@Composable
fun EditPhotoPage(modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val activity = LocalContext.current as MainActivity
    //導入圖片
    val imageUrl by remember { mutableStateOf(readDataClass(context, "ImageUrl", "")) }
    //畫布圖片
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    //畫布控制元件
    val sketchbookController = rememberSketchbookController()
    Log.i("linpoi", "EditPhotoPage,$imageUrl")
    /*
        *****將url轉換成imageBitmap*****

     */
    val inputStream = context.contentResolver.openInputStream(imageUrl.toUri())
    inputStream?.use { stream ->

        Log.i("0123", "新增圖片")
        //解碼圖片
        var bitmap = BitmapFactory.decodeStream(stream)
        bitmap = rotateBitmap(bitmap, 90f)
        //丟回圖片變數
        imageBitmap = bitmap.asImageBitmap()
        //保存圖片
    }

    Scaffold(modifier = modifier.fillMaxSize(),
        //頂部欄位
        topBar = {
            Row(
                modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "編輯圖片",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        //底部欄位
        bottomBar = {
            Row(
                modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    //撤回
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                sketchbookController.undo() // undo the drawn path if possible.
                            })
                    //修復
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .size(30.dp)
                            .clickable {
                                sketchbookController.redo() // redo the drawn path if possible.
                            })
                }
                //傳送
                Icon(
                    Icons.Filled.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(30.dp)

                        .clickable {
                            val bitmap = sketchbookController.getSketchbookBitmap().asAndroidBitmap()
                            saveBitmapAsPNG(bitmap, activity)

                        })
            }
            // 根據狀態變化執行相應的操作
        }
    ) {
        //來源https://github.com/GetStream/sketchbook-compose
        //設定畫筆顏色
        sketchbookController.setPaintColor(Color(255,88,9))
        //畫布
        Sketchbook(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            controller = sketchbookController,
            imageBitmap = imageBitmap
        )
    }

    //返回
    HandleBackPress {
        navController.navigate(Screens.CameraPage.name)
    }
}

fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )
}