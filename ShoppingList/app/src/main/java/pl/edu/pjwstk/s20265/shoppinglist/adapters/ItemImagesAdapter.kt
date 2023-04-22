package pl.edu.pjwstk.s20265.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjwstk.s20265.shoppinglist.R
import pl.edu.pjwstk.s20265.shoppinglist.databinding.EditPickerImageBinding

class ItemImageViewHolder(val binding: EditPickerImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(resId: Int, isSelected: Boolean) {
        binding.editPickerSelectedBackground.visibility =
            if (isSelected) View.VISIBLE else View.INVISIBLE
        binding.editPickerImage.setImageResource(resId)
    }
}


class ItemImagesAdapter : RecyclerView.Adapter<ItemImageViewHolder>() {
    private val images = listOf(R.drawable.cake, R.drawable.cherry)
    private var selectedPosition: Int = 0
    val selectedResId: Int
        get() = images[selectedPosition]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewHolder {
        val binding = EditPickerImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemImageViewHolder(binding).also { vh ->
            binding.root.setOnClickListener {
                notifyItemChanged(selectedPosition)
                selectedPosition = vh.layoutPosition
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemImageViewHolder, position: Int) {
        holder.bind(images[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = images.size

}