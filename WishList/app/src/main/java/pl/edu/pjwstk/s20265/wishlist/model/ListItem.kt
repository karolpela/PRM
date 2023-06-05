package pl.edu.pjwstk.s20265.wishlist.model

import android.net.Uri
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

data class ListItem(
    val id: Long,
    val name: String,
    val photoUri: Uri,
    val location: LatLng?,
    val locationString: String?,
    val addedOn: LocalDateTime
)