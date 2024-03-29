package pl.edu.pjwstk.s20265.wishlist.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjwstk.s20265.wishlist.ListItemCallback
import pl.edu.pjwstk.s20265.wishlist.MainActivity
import pl.edu.pjwstk.s20265.wishlist.Navigable
import pl.edu.pjwstk.s20265.wishlist.databinding.ListItemBinding
import pl.edu.pjwstk.s20265.wishlist.model.ListItem

class ListItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(listItem: ListItem) {
        binding.itemName.text = listItem.name
        binding.itemLocationString.text = listItem.locationString

        if (listItem.photoUri.toString() != "null") {
            binding.itemImage.photo =
                binding.root.context.applicationContext.contentResolver.openInputStream(listItem.photoUri)
                    .use { BitmapFactory.decodeStream(it) }
        }
        binding.itemImage.text = listItem.note
    }
}


class ListItemsAdapter : RecyclerView.Adapter<ListItemViewHolder>() {

    internal val data = mutableListOf<ListItem>()

    //    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var selectedIndex: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding).also { vh ->
            binding.root.setOnClickListener {
                (it.context as? Navigable)?.navigate(
                    Navigable.Destination.Edit, data[vh.layoutPosition].id
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
        val callback = ListItemCallback(data.toList(), newData)
        data.clear()
        data.addAll(newData)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
    }

    fun sort() {
        val notSorted = data.toList() // makes a copy of data
        data.sortBy { it.addedOn }
        val callback = ListItemCallback(notSorted, data)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
    }

    fun removeItem(layoutPosition: Int): ListItem {
        val item = data.removeAt(layoutPosition)
        notifyItemRemoved(layoutPosition)
        return item
    }

}
