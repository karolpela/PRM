package pl.edu.pjwstk.s20265.shoppinglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjwstk.s20265.shoppinglist.databinding.ListItemBinding
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

//TODO create a viewholder for total and items checked?

class ListItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(listItem: ListItem) {
        binding.itemName.text = listItem.name
        binding.itemPrice.text = priceFormat.format(listItem.price)
        binding.itemImage.setImageResource(listItem.resId)
    }

    companion object {
        val priceFormat: NumberFormat = NumberFormat.getCurrencyInstance().also {
            it.maximumFractionDigits = 2
        }
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
        return data.sumOf { item -> item.price }
    }
}

