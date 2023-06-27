package pl.edu.pjwstk.s20265.wishlist.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import pl.edu.pjwstk.s20265.wishlist.Notifications

private const val NOTIFICATION_ID = 1

class AlertService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, Notifications.createNotification(this))
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null
}