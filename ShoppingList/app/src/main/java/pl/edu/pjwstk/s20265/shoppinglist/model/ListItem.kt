package pl.edu.pjwstk.s20265.shoppinglist.model

import androidx.annotation.DrawableRes
import java.math.BigDecimal

data class ListItem(
    val name: String,
    val price: BigDecimal,
    val count: Int,
    val notes: String,
    @DrawableRes val resId: Int
)
