package com.gmail.notifyu

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// to show notification on the foreground
class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title : String? = message.notification!!.title
        val body : String? = message.notification!!.body
        val action : String? = message.notification!!.clickAction

        // create intent from the click action
        val intent = Intent(action)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            // flag will update the current screen
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        showNotification(title!!, body!!, pendingIntent)
    }

    private fun showNotification(title: String, body: String, pendingIntent: PendingIntent?) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // creating notification channel
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("Channel_Id", "message", importance)

            // registering channel with the system
            val manager:NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(mChannel)
        }

        val mBuilder : NotificationCompat.Builder = NotificationCompat
            .Builder(this, "Channel_Id")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager : NotificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManager.notify(123, mBuilder.build())
    }
}