package pl.edu.pjwstk.s20265.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjwstk.s20265.shoppinglist.R
import pl.edu.pjwstk.s20265.shoppinglist.data.DataSource
import pl.edu.pjwstk.s20265.shoppinglist.databinding.ListItemBinding
import pl.edu.pjwstk.s20265.shoppinglist.model.ListItem
import java.math.BigDecimal

//TODO create a viewholder for total and items checked?

class ListItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(listItem: ListItem) {
        binding.itemName.text = listItem.name
        binding.itemPrice.text = DataSource.priceFormat.format(listItem.price)
        binding.itemCount.text =
            itemView.context.getString(R.string.list_item_count, listItem.count)
        binding.itemImage.setImageResource(listItem.resId)
    }


}

class ListItemsAdapter : RecyclerView.Adapter<ListItemViewHolder>() {

    private val data = mutableListOf<ListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun replace(newData: List<ListItem>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged() //TODO replace with DiffUtil?
    }

    fun getTotalPrice(): BigDecimal {
        return data.sumOf { item -> item.price * BigDecimal(item.count) }
    }
}

