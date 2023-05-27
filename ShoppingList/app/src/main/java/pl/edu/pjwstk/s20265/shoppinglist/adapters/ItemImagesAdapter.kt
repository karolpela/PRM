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

class ItemImagesAdapter(resId: Int?) : RecyclerView.Adapter<ItemImageViewHolder>() {
    private var images =
        listOf(
            R.drawable.item_cake,
            R.drawable.item_cherry,
            R.drawable.item_donut,
            R.drawable.item_eggplant,
            R.drawable.item_grape,
            R.drawable.item_icecream,
            R.drawable.item_mushroom,
            R.drawable.item_onigiri,
            R.drawable.item_peach,
            R.drawable.item_pear,
            R.drawable.item_pomelo,
            R.drawable.item_shrimp,
            R.drawable.item_strawberry,
            R.drawable.item_tomato,
            R.drawable.item_watermelon,
        ) // TODO maybe move to arrays.xml

    //    private var images = ArrayList<Int>()

    private var selectedPosition: Int = if (resId != null) images.indexOf(resId) else 0
    val selectedResId: Int
        get() = images[selectedPosition]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewHolder {
        //        images.addAll(parent.context.resources.getIntArray(R.array.items).asList())

        val binding =
            EditPickerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
