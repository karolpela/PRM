package pl.edu.pjwstk.s20265.shoppinglist.data

import pl.edu.pjwstk.s20265.shoppinglist.R
import pl.edu.pjwstk.s20265.shoppinglist.model.ListItem
import java.math.BigDecimal
import java.text.NumberFormat

object DataSource {
    val listItems = mutableListOf(
        ListItem("Cake", BigDecimal("4.99"), 1, "Chocolate cake", R.drawable.item_cake),
        ListItem("Cherry", BigDecimal("0.09"), 20, "Cherry", R.drawable.item_cherry),
        ListItem("Peach", BigDecimal("0.79"), 3, "Peach", R.drawable.item_peach)
    )

    val priceFormat: NumberFormat = NumberFormat.getCurrencyInstance().also {
        it.maximumFractionDigits = 2
    }
}