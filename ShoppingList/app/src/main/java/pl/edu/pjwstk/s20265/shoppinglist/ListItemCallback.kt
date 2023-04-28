package pl.edu.pjwstk.s20265.shoppinglist

import androidx.recyclerview.widget.DiffUtil
import pl.edu.pjwstk.s20265.shoppinglist.model.ListItem

class ListItemCallback(val notSorted: List<ListItem>, val sorted: List<ListItem>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = notSorted.size

    override fun getNewListSize(): Int = sorted.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return notSorted[oldItemPosition] === sorted[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // This works because ListItem is a dataclass, so it has equals() implemented
        return notSorted[oldItemPosition] == sorted[newItemPosition]
    }
}
