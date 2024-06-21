package com.example.ripcurrent.page

import android.graphics.Bitmap
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ripcurrent.Data.Member
import com.example.ripcurrent.MainActivity
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.backHandler.HandleBackPress
import com.example.ripcurrent.tool.savedataclass.readDataClass
import com.example.ripcurrent.tool.savedataclass.readDataClass_Bitmap
import com.example.ripcurrent.tool.savedataclass.saveDataClass
import com.example.ripcurrent.tool.hint.showToast
import io.getstream.sketchbook.Sketchbook
import io.getstream.sketchbook.rememberSketchbookController

@Composable
fun EditPhotoPage(modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val activity = LocalContext.current as MainActivity
    //導入圖片
    val picture by remember { mutableStateOf(readDataClass_Bitmap(context, "updatePicture")) }
    //畫布圖片

    //畫布控制元件
    val sketchbookController = rememberSketchbookController()
    Log.i("linpoi", "EditPhotoPage,$picture")
    /*
        *****將url轉換成imageBitmap*****

     */



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
                        Icons.AutoMirrored.Filled.ArrowForward,
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
                    Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(30.dp)

                        .clickable {
                            var member = readDataClass(context, "Member") ?: Member()
                            saveDataClass(context, "SourceNav", Screens.EditPhotoPage.name)
                            if (member.MemberGmail == "") {
                                showToast(context, R.string.please_log_in_first)
                                navController.navigate(Screens.LoginPage.name)
                            } else {
                                val bitmap = sketchbookController
                                    .getSketchbookBitmap()
                                    .asAndroidBitmap()
                                //SaveBitmapAsPNG(bitmap, activity, member)
                                saveDataClass(context, "bitmap", bitmap)
                                navController.navigate(Screens.EditPhotoInformationPage.name)
                            }
                        })
            }
            // 根據狀態變化執行相應的操作
        }
    ) {
        //來源https://github.com/GetStream/sketchbook-compose
        //設定畫筆顏色
        sketchbookController.setPaintColor(Color(255,88,9))
        sketchbookController.setPaintAlpha(0.4f)
        sketchbookController.setPaintStrokeWidth(100f)
        //畫布
        Sketchbook(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            controller = sketchbookController,
            imageBitmap = picture?.asImageBitmap()
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