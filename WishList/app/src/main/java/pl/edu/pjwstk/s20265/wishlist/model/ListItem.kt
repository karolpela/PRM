package pl.edu.pjwstk.s20265.wishlist.model

import android.net.Uri
import androidx.annotation.DrawableRes

data class ListItem(
    val id: Long,
    val name: String,
    val photoUri: Uri
)
