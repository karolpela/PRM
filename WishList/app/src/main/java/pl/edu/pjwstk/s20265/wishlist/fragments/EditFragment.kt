package pl.edu.pjwstk.s20265.wishlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.edu.pjwstk.s20265.wishlist.data.ListItemDatabase
import pl.edu.pjwstk.s20265.wishlist.data.model.ListItemEntity
import pl.edu.pjwstk.s20265.wishlist.databinding.FragmentEditBinding
import kotlin.concurrent.thread

private const val ARG_LIST_ITEM_ID = "list_item_id"

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var database: ListItemDatabase
    private var listItem: ListItemEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = ListItemDatabase.open(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listItemId = requireArguments().getLong(ARG_LIST_ITEM_ID, -1)
        if (listItemId != -1L) {
            thread {
                listItem = database.listItems.getListItem(listItemId)
                requireActivity().runOnUiThread {
                    binding.editItemName.setText(listItem?.name ?: "")
                }
            }
        }

        binding.editButtonSave.setOnClickListener {
            val listItem = listItem?.copy(
                name = binding.editItemName.text.toString(),
                photo = resources.getResourceEntryName(0)
            ) ?: ListItemEntity(
                name = binding.editItemName.text.toString(),
                photo = resources.getResourceEntryName(0)
            )
            this.listItem = listItem

            thread {
                database.listItems.addListItem(listItem) // updates if item exists
            }
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
