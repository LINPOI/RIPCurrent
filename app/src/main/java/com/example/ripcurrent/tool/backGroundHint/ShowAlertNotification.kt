package com.example.ripcurrent.tool.backGroundHint

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ripcurrent.MainActivity
import com.example.ripcurrent.R

 const val CHANNEL_ID = "alert_channel_id"

fun createNotificationChannel(context: Context) {
    val name = "Alert Channel"
    val descriptionText = "Channel for alert notifications"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val RingType: Int = RingtoneManager.TITLE_COLUMN_INDEX
    val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
        description = descriptionText
        // 設置震動模式
        enableVibration(true)
        vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 500)
        // 設置音效
        setSound(RingtoneManager.getDefaultUri(RingType), null)
    }
    // 註冊渠道
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

fun ShowAlertNotification(context: Context) {
        createNotificationChannel(context)
    val RingType: Int = RingtoneManager.TITLE_COLUMN_INDEX
    // 構建通知
    val notificationId = 1
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ripcurrent) // 替換為您應用的圖標
        .setContentTitle(context.getString(R.string.warning))
        .setContentText(context.getString(R.string.there_is_a_rip_current_occurring_nearby))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        // 設置震動模式
        .setVibrate(longArrayOf(0, 500, 200, 500, 200, 500))
        // 設置音效
        .setSound(RingtoneManager.getDefaultUri(RingType))

    // 顯示通知
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(notificationId, builder.build())
    }
}
