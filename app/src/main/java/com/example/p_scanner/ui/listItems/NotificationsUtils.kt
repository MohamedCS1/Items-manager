package com.example.p_scanner.ui.listItems

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.p_scanner.R


class NotificationsUtils(val context: Context) {

    private val channelId = "1"
    private val notificationId = 1

     fun displayNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(channelId ,"Notification Export Channel" ,NotificationManager.IMPORTANCE_HIGH)

            val notificationManager = getSystemService(context ,NotificationManager ::class.java)

            notificationManager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context,channelId)

        val notificationManager = NotificationManagerCompat.from(context)

         val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)


         val pendingIntent = PendingIntent.getActivities(context ,0 , arrayOf(intent),0)

        notification.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentTitle("Exported successfully")
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId ,notification.build())

    }




}