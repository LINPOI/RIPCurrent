package com.example.ripcurrent.tool

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens

@Composable
fun OpenAlbumScreen(
    imageCapture: ImageCapture, context: Context,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    //容納圖片的變數
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    val singleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
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
                                  //         saveImageToInternalStorage(context, member, bitmap, fileName)
                    }
                    navController.navigate(Screens.EditPhotoPage.name)
                } catch (e: Exception) {
                    Log.e("0123", "Error: ${e.message}")
                }
            }
        }
    )

    val multiLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(9),
        onResult = {
            //TODO
        }
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 0.dp, start = 3.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(end = 10.dp, start = 20.dp)
                .size(70.dp)
            ,
            shape = MaterialTheme.shapes.extraSmall,
            onClick = {
                singleLauncher.launch(PickVisualMediaRequest())
            }
        ) {
            Text(text = stringResource(R.string.open_photo_album), )
        }
        Icon(
            Icons.TwoTone.AddCircle , contentDescription = null,
            modifier = Modifier
                .padding(50.dp)
                .size(70.dp)
                .clickable {
                    captureImage(imageCapture, context,navController)
                    showToast(context, R.string.shooting_successful)
                }
        )
        Box(modifier = modifier){
            Spacer(modifier = Modifier.padding(horizontal = 60.dp))
        }
//        Button(
//            onClick = {
//                multiLauncher.launch(PickVisualMediaRequest())
//            }
//        ) {
//            Text(text = "多選")
//        }

    }
}