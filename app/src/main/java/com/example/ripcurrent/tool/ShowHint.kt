package com.example.ripcurrent.tool

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


//顯示對話框
@SuppressLint("SuspiciousIndentation")
@Composable
fun ShowAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var openDialog by remember { mutableStateOf(true) }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(
                    onClick ={
                        openDialog=false
                        onConfirm()
                    } ,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "確定", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog=false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "取消", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        )
    }
}

private var lastToast: Toast? = null// 記錄上一個 Toast 的實例，以便取消

fun showToast(context: Context, message: Int, insurance: Boolean = false) {
    if (insurance) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()//訊息文字、顯示時長
        }
    } else {
        // 取消先前的 Toast
        lastToast?.cancel()

        // 顯示新的 Toast
        lastToast = Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT)
        lastToast?.show()
    }


}

