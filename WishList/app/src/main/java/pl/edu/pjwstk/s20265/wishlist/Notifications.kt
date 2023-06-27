package pl.edu.pjwstk.s20265.wishlist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

private const val CHANNEL_ID = "ID_CHANNEL_DEFAULT"

object Notifications {

    fun createChannel(context: Context) {
        val channel =
            NotificationChannel(CHANNEL_ID, "Wish List", NotificationManager.IMPORTANCE_DEFAULT)
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun createNotification(context: Context) = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Saved item nearby!")
        .setContentText("Open Wish List and check now")
        .build()
}