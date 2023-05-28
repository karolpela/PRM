package pl.edu.pjwstk.s20265.wishlist

interface Navigable {

    enum class Destination {
        List,
        Add,
        Edit,
    }

    fun navigate(to: Destination, id: Long? = null)
}
