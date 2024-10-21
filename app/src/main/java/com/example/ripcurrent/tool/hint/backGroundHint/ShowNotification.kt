package com.example.ripcurrent.tool.hint.backGroundHint

import android.Manifest
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ripcurrent.MainActivity
import com.example.ripcurrent.R
import com.example.ripcurrent.tool.savedataclass.saveDataClass


fun ShowNotification(context: Context,location:String) {
    val title= context.getString(R.string.warning)
    val message=context.getString(R.string.there_is_a_rip_current_occurring_nearby,location)
    val RingType: Int = RingtoneManager.TITLE_COLUMN_INDEX
    createNotificationChannel(context)
    // 建立 Intent，用於啟動應用的主 Activity
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // 構建通知
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ripcurrent)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setContentIntent(pendingIntent)  // 設置 PendingIntent
        .setFullScreenIntent(pendingIntent, true)
        .setAutoCancel(true)  // 點擊後自動取消通知
        // 設置震動模式
        .setVibrate(longArrayOf(0, 500, 200, 500, 200, 500))
        // 設置音效
        .setSound(RingtoneManager.getDefaultUri(RingType))

    // 檢查通知權限並發送通知
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果權限未授予，則不發送通知
            return
        }
    }
    // 發送通知
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notify(1, builder.build())
    }
    val mediaPlayer = MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
    // 使用對話框彈出 Alert
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle(title)
    alertDialogBuilder.setMessage(message)
    alertDialogBuilder.setPositiveButton(context.getString(R.string.ok))
        { dialog, _ ->
            dialog.dismiss()
            // 停止音效
            mediaPlayer.stop()
            mediaPlayer.release()
            // 取消震動
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.cancel()
            saveDataClass(context,"AlertState",false)
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        // 震動
    if(false){
        // 播放音效

        mediaPlayer.setAudioAttributes(AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build())
        mediaPlayer.isLooping = true // 音效循環
        // 震動
        mediaPlayer.start()
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 1000, 500, 1000), 0))
    }


}