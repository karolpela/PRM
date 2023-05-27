package pl.edu.pjwstk.s20265.wishlist

interface Navigable {

    enum class Destination {
        List,
        Add,
        Edit,
        Details
    }

    fun navigate(to: Destination, id: Long? = null)
}
