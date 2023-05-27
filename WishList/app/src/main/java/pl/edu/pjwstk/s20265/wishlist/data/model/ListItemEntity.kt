package pl.edu.pjwstk.s20265.wishlist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listItem")
data class ListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val photo: String,
    //TODO add location
)