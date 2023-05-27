package pl.edu.pjwstk.s20265.wishlist.model

import androidx.annotation.DrawableRes
import java.math.BigDecimal

data class ListItem(
    val id: Long,
    val name: String,
    @DrawableRes val resId: Int
)
