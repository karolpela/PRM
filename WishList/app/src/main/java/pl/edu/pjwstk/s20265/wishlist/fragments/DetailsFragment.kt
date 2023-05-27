package pl.edu.pjwstk.s20265.wishlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.edu.pjwstk.s20265.wishlist.Navigable
import pl.edu.pjwstk.s20265.wishlist.data.ListItemDatabase
import pl.edu.pjwstk.s20265.wishlist.data.model.ListItemEntity
import pl.edu.pjwstk.s20265.wishlist.databinding.FragmentDetailsBinding
import kotlin.concurrent.thread

private const val ARG_LIST_ITEM_ID = "list_item_id"

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var database: ListItemDatabase
    private lateinit var listItem: ListItemEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = ListItemDatabase.open(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentDetailsBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listItemId = requireArguments().getLong(ARG_LIST_ITEM_ID, -1)
        if (listItemId != -1L) {
            thread {
                listItem = database.listItems.getListItem(listItemId)
                requireActivity().runOnUiThread {
                    binding.detailsItemName.text = listItem.name
//                    binding.detailsItemImage.setImageResource(
//                        resources.getIdentifier(listItem.photoUriString, "drawable", requireContext().packageName)
//                    )
                }
            }
        }

        binding.detailsButtonEdit.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Edit, listItem.id)
        }
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
