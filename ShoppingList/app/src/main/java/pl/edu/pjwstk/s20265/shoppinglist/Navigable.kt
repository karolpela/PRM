package pl.edu.pjwstk.s20265.shoppinglist

interface Navigable {

    enum class Destination {
        List, Add, Edit, Details
    }

    fun navigate(to: Destination, itemIndex: Int = -1)
}