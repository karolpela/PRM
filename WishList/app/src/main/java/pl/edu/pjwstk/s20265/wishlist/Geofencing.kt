package pl.edu.pjwstk.s20265.wishlist

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import pl.edu.pjwstk.s20265.wishlist.service.AlertService

const val FENCE_RADIUS = 200f
private const val REQUEST_CODE = 2

object Geofencing {
    fun createGeofence(context: Context, latLng: LatLng, id: Long) {
        val geofence =
            Geofence.Builder().setCircularRegion(latLng.latitude, latLng.longitude, FENCE_RADIUS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER) // removed DWELL, hope this still works
                .setRequestId("item${id}")
                .setExpirationDuration(Geofence.NEVER_EXPIRE).build()

        val request = GeofencingRequest.Builder().addGeofence(geofence)
            .setInitialTrigger(0).build()

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            ).let {
                context.startActivity(it)
            }
        } else {
            LocationServices.getGeofencingClient(context)
                .addGeofences(request, makePendingIntentForAlert(context))
                .addOnFailureListener { println(it) }
                .addOnSuccessListener { println("Geofence added") }
        }

    }

    fun makePendingIntentForAlert(context: Context): PendingIntent =
        PendingIntent.getForegroundService(
            context,
            REQUEST_CODE,
            Intent(context, AlertService::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
}