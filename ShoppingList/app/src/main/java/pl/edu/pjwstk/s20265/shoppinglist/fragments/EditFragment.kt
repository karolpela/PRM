package pl.edu.pjwstk.s20265.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjwstk.s20265.shoppinglist.adapters.ItemImagesAdapter
import pl.edu.pjwstk.s20265.shoppinglist.data.DataSource
import pl.edu.pjwstk.s20265.shoppinglist.databinding.FragmentEditBinding
import pl.edu.pjwstk.s20265.shoppinglist.model.ListItem

class EditFragment(private val itemIndex: Int) : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: ItemImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var item: ListItem? = null;
        if (itemIndex != -1) {
            item = DataSource.listItems[itemIndex]
            binding.editItemName.setText(item.name)
            binding.editItemPrice.setText(item.price.toString())
            binding.editItemCount.setText(item.count.toString())
            binding.editItemNotes.setText(item.notes)
        }

        adapter = ItemImagesAdapter(item?.resId)

        binding.editItemImagePicker.apply {
            adapter = this@EditFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.editButtonSave.setOnClickListener {
            val newItem = ListItem(
                binding.editItemName.text.toString(),
                binding.editItemPrice.text.toString().toBigDecimal(),
                binding.editItemCount.text.toString().toInt(),
                binding.editItemNotes.text.toString(),
                adapter.selectedResId
            )

            if (itemIndex != -1) {
                DataSource.listItems[itemIndex] = newItem
            } else {
                DataSource.listItems.add(newItem)
            }

            // This makes the transition back to List fragment slide out instead of sliding in
            parentFragmentManager.popBackStack()
        }
    }
}