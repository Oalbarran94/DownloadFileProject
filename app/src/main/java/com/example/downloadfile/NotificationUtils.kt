package com.example.downloadfile

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap

private const val NOTIFICATION_ID = 0
const val DOWNLOAD_ID = "DOWNLOAD_ID"

fun NotificationManager.sendNotification(
    messageBody: String,
    downloadId: Long,
    channelId: String,
    context: Context
) {
        val bundle = Bundle()
        bundle.putLong(DOWNLOAD_ID, downloadId)
        bundle.putString("TEST", "test")

        val contentIntent = Intent(context, DetailActivity::class.java)
        contentIntent.putExtras(bundle)

        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val bmp = AppCompatResources
            .getDrawable(context, R.mipmap.ic_launcher)?.toBitmap()

        val style = NotificationCompat.BigPictureStyle()
            .bigPicture(bmp)
            .setBigContentTitle(context.getString(R.string.notificationTitle))

        val builder = NotificationCompat.Builder(
            context,
            channelId
        ).setSmallIcon(androidx.core.R.drawable.notification_bg)
            .setContentTitle(context.getString(R.string.notificationTitle))
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setPriority(Notification.PRIORITY_MAX)
            .setChannelId(channelId)
            .setAutoCancel(true)
            .addExtras(bundle)
            .setExtras(bundle)
            .setStyle(style)
            .addAction(
                androidx.core.R.drawable.notification_bg,
                context.getString(R.string.notificationButton),
                contentPendingIntent
            )
        notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.createChannel(channelId: String, channelName: String) {

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = "Download completed!"
        }

        createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.cancelNotifications(){
    cancelAll()
}