package pl.edu.pjwstk.s20265.wishlist.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjwstk.s20265.wishlist.ListItemCallback
import pl.edu.pjwstk.s20265.wishlist.MainActivity
import pl.edu.pjwstk.s20265.wishlist.Navigable
import pl.edu.pjwstk.s20265.wishlist.databinding.ListItemBinding
import pl.edu.pjwstk.s20265.wishlist.model.ListItem

// TODO create a viewholder for total and items checked?

class ListItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(listItem: ListItem) {
        binding.itemName.text = listItem.name
//        binding.itemImage.setImageResource(listItem.photoUri)
    }
}

class ListItemsAdapter : RecyclerView.Adapter<ListItemViewHolder>() {

    internal val data = mutableListOf<ListItem>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var selectedIndex: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding).also { vh ->
            binding.root.setOnClickListener {
                (it.context as? Navigable)?.navigate(
                    Navigable.Destination.Details,
                    data[vh.layoutPosition].id
                )
            }
            binding.root.setOnLongClickListener {
                selectedIndex = vh.layoutPosition
                (parent.context as MainActivity).showDeleteDialog()
                return@setOnLongClickListener true // no cleaner way to do this?
            }
        }
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun replace(newData: List<ListItem>) {
        val callback = ListItemCallback(data, newData)
        data.clear()
        data.addAll(newData)
        val result = DiffUtil.calculateDiff(callback)
        handler.post { result.dispatchUpdatesTo(this) }
    }

    fun sort() {
        val notSorted = data.toList() // makes a copy of data
        data.sortBy { it.name.lowercase() }
        val callback = ListItemCallback(notSorted, data)
        val result = DiffUtil.calculateDiff(callback)
        handler.post { result.dispatchUpdatesTo(this) }
    }

    fun removeItem(layoutPosition: Int): ListItem {
        val item = data.removeAt(layoutPosition)
        notifyItemRemoved(layoutPosition)
        return item
    }

}
