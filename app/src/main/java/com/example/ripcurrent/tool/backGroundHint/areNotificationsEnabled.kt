package com.example.ripcurrent.tool.backGroundHint

import android.app.NotificationManager
import android.content.Context

/**
 * 檢查是否開啟推播
 */
fun Context.areNotificationsEnabled(): Boolean {
    val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.areNotificationsEnabled()
}