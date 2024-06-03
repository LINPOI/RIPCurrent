package com.example.ripcurrent.tool.backGroundHint

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
//https://medium.com/@yenahong880117/%E4%BD%BF%E7%94%A8compose%E9%96%8B%E5%95%9Fapp%E9%80%9A%E7%9F%A5%E6%AC%8A%E9%99%90-79626f5e56d8
@Composable
fun SampleScreen() {
    val context = LocalContext.current
    var showOpenNotificationDialog: Boolean by remember { mutableStateOf(false) }
    //彈窗跳轉
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (context.areNotificationsEnabled()) {
                openNotificationAction(context)
            } else {
                noNotificationAction(context)
            }
        }
    Button(onClick = {
        if (context.areNotificationsEnabled()) {
            openNotificationAction(context)
        } else {
            showOpenNotificationDialog = true
        }
    }) {
        Text(text = "檢查推播權限")
    }

    if (showOpenNotificationDialog) {
        OpenNotificationDialog(
            onDismiss = { showOpenNotificationDialog = false },
            launcher = launcher
        )
    }
}

/**
 * @param onDismiss 彈窗消失
 * @param launcher 系統通知頁回傳
 */
@Composable
private fun OpenNotificationDialog(
    onDismiss: () -> Unit,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "開啟推播權限", modifier = Modifier.padding(vertical = 20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
            ) {
                Button(
                    onClick = {
                        noNotificationAction(context)
                        onDismiss.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight()
                ) {
                    Text(text = "放棄")
                }

                Button(
                    onClick = {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                        launcher.launch(intent)
                        onDismiss.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text(text = "開啟")
                }
            }
        }
    }
}

private fun openNotificationAction(context: Context) {
    Toast.makeText(context, "推播權限已開", Toast.LENGTH_SHORT).show()
}

private fun noNotificationAction(context: Context) {
    Toast.makeText(context, "推播權限未開", Toast.LENGTH_SHORT).show()
}