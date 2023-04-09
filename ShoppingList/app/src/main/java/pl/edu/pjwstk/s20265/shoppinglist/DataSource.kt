package pl.edu.pjwstk.s20265.shoppinglist

import java.math.BigDecimal

object DataSource {
    val listItems = mutableListOf(
        ListItem("Cake", 1, BigDecimal(4.99), "Chocolate cake", R.drawable.cake),
        ListItem("Cherry", 20, BigDecimal(0.09), "Cherry", R.drawable.cherry),
        ListItem("Peach", 3, BigDecimal(0.79), "Peach", R.drawable.peach)
    )
}