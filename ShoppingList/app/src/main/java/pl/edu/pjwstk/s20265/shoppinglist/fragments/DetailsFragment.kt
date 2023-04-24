package pl.edu.pjwstk.s20265.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.edu.pjwstk.s20265.shoppinglist.Navigable
import pl.edu.pjwstk.s20265.shoppinglist.R
import pl.edu.pjwstk.s20265.shoppinglist.data.DataSource
import pl.edu.pjwstk.s20265.shoppinglist.databinding.FragmentDetailsBinding
import pl.edu.pjwstk.s20265.shoppinglist.model.ListItem

class DetailsFragment(private val itemId: Int) : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var item: ListItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentDetailsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item = DataSource.listItems[itemId]

        binding.detailsItemImage.setImageResource(item.resId)

        binding.detailsItemName.text = item.name
        binding.detailsItemPrice.text = DataSource.priceFormat.format(item.price)
        binding.detailsItemCount.text = getString(R.string.list_item_count, item.count)
        binding.detailsItemNotes.text = item.notes


        binding.detailsButtonEdit.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Edit, itemId)
        }
    }
}