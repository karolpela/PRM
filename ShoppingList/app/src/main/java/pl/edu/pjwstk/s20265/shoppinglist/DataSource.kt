package pl.edu.pjwstk.s20265.shoppinglist

import java.math.BigDecimal
import java.text.NumberFormat

object DataSource {
    val listItems = mutableListOf(
        ListItem("Cake", BigDecimal(4.99), 1, "Chocolate cake", R.drawable.cake),
        ListItem("Cherry", BigDecimal(0.09), 20, "Cherry", R.drawable.cherry),
        ListItem("Peach", BigDecimal(0.79), 3, "Peach", R.drawable.peach)
    )

    val priceFormat: NumberFormat = NumberFormat.getCurrencyInstance().also {
        it.maximumFractionDigits = 2
    }
}