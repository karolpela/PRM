package pl.edu.pjwstk.s20265.shoppinglist

import androidx.annotation.DrawableRes
import java.math.BigDecimal

data class ListItem(
    val name: String,
    val count: Int,
    val price: BigDecimal,
    val note: String,
    @DrawableRes
    val resId: Int
)
