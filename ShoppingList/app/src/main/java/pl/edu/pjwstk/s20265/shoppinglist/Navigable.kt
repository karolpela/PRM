package pl.edu.pjwstk.s20265.shoppinglist

interface Navigable {

    enum class Destination {
        List, Add
    }

    fun navigate(to: Destination)
}