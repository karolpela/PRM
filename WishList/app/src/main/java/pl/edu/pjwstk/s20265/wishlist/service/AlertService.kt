package pl.edu.pjwstk.s20265.wishlist.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.location.GeofencingEvent
import pl.edu.pjwstk.s20265.wishlist.Notifications

private const val NOTIFICATION_ID = 1

class AlertService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val geofence = intent?.let { GeofencingEvent.fromIntent(it) }
        val id = geofence?.triggeringGeofences?.firstOrNull()?.requestId
        startForeground(NOTIFICATION_ID, Notifications.createNotification(this))
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null
}