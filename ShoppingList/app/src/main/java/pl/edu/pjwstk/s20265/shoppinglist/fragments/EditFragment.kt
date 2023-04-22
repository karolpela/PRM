package pl.edu.pjwstk.s20265.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjwstk.s20265.shoppinglist.ItemImagesAdapter
import pl.edu.pjwstk.s20265.shoppinglist.model.ListItem
import pl.edu.pjwstk.s20265.shoppinglist.data.DataSource
import pl.edu.pjwstk.s20265.shoppinglist.databinding.FragmentEditBinding

class EditFragment : Fragment() {

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

        adapter = ItemImagesAdapter()

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
            DataSource.listItems.add(newItem)

            // This makes the transition back to List fragment slide out instead of sliding in
            parentFragmentManager.popBackStack()
        }
    }
}