package pl.edu.pjwstk.s20265.wishlist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

@Entity(tableName = "listItem")
data class ListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val photoUriString: String,
    val latitude: Double,
    val longitude: Double,
    val addedOn: LocalDateTime
    //TODO add location
)