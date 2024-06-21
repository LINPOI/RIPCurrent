package com.example.ripcurrent.tool.backHandler

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.example.ripcurrent.R
import com.example.ripcurrent.tool.hint.showToast

private var lastToast: Toast? = null
@Composable
fun DoubleBackHandler(//雙擊滑鼠
    context: Context,
    enabled: Boolean = true,
    //onBackPressed: () -> Unit={null},

) {
    val duration = 2000 // 兩秒

    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = enabled) {
        if (backPressedTime + duration > System.currentTimeMillis()) {

            exitApp(context)
        }else{
            showToast(context, R.string.press_back_again_to_exit)

        }
        backPressedTime = System.currentTimeMillis()
    }
}
@Composable
fun HandleBackPress(onBackPressed: () -> Unit) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val backCallback = rememberUpdatedState(onBackPressed)

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backCallback.value()
            }
        }
    }

    DisposableEffect(dispatcher) {
        dispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }
}
@Composable
fun BackHandlerPress(onBackPressed: () -> Unit){
    BackHandler(enabled = true) {
        onBackPressed()
    }
}
fun exitApp(context: Context) {
    lastToast?.cancel()
    (context as? Activity)?.finish()
    System.exit(0)
}