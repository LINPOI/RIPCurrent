package com.example.ripcurrent.tool.pictureTool

import ImageClassifier
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.hint.showToast
import com.example.ripcurrent.tool.position.GetPosition
import com.example.ripcurrent.tool.savedataclass.saveDataClass

@Composable
fun OpenAlbumScreen(
    imageCapture: ImageCapture, context: Context,
    modifier: Modifier = Modifier,
    navController: NavHostController=NavHostController(context),
) {

    //容納圖片的變數
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var position by remember { mutableStateOf("") }
    val imageClassifier = remember { ImageClassifier(context,"model.tflite") }
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
                        var bitmap = BitmapFactory.decodeStream(stream)
                        /// 使用 ImageClassifier 進行辨識
                        val targetFound = imageClassifier.classify(bitmap)

                        // 根據分類結果顯示訊息
                        if (targetFound) {
                            Log.i("ImageClassifier", "目標已發現")
                            bitmap=imageClassifier.classifyAndMark(bitmap)
                        } else {
                            Log.i("ImageClassifier", "未發現目標")
                        }
                        saveDataClass(context,"updatePicture",bitmap)
                        //丟回圖片變數
                        selectedImage = bitmap?.asImageBitmap()
                        //保存圖片
                                  //         saveImageToInternalStorage(context, member, bitmap, fileName)
                    }
                    navController.navigate(Screens.EditPhotoPage.name)
                } catch (e: Exception) {
                    Log.e("linpoi", "singleLauncherError: ${e.message}")
                    saveDataClass(context,"ImageUrl", "")
                    showToast(context, R.string.the_photo_you_selected_is_not_supported)
                    navController.navigate(Screens.CameraPage.name)
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
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id =  R.drawable.photo_library_24px), contentDescription = null,modifier = Modifier.padding(start = 25.dp).clickable {try {
            singleLauncher.launch(PickVisualMediaRequest())
        }catch (e:Exception){
            Log.e("linpoi", "Error: OpenAlbumScreen${e.message}")

        }  })
        Icon(
            Icons.TwoTone.AddCircle , contentDescription = null,
            modifier = Modifier
                .padding(50.dp)
                .size(70.dp)
                .clickable {
                    captureImage(imageCapture, context, navController, position)
                    showToast(context, R.string.shooting_successful)
                }
        )
        position= GetPosition(modifier.size(100.dp))
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