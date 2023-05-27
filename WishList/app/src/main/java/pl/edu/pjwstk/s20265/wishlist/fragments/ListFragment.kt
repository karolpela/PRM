package pl.edu.pjwstk.s20265.wishlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjwstk.s20265.wishlist.Navigable
import pl.edu.pjwstk.s20265.wishlist.R
import pl.edu.pjwstk.s20265.wishlist.adapters.ListItemsAdapter
import pl.edu.pjwstk.s20265.wishlist.data.ListItemDatabase
import pl.edu.pjwstk.s20265.wishlist.databinding.FragmentListBinding
import pl.edu.pjwstk.s20265.wishlist.model.ListItem
import pl.edu.pjwstk.s20265.wishlist.viewmodel.MainViewModel
import kotlin.concurrent.thread

class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    lateinit var adapter: ListItemsAdapter
    lateinit var database: ListItemDatabase
    val vm: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = ListItemDatabase.open(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onResume() {
        super.onResume()
        adapter.sort()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListItemsAdapter()
        loadData()

        binding.listItems.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this.context)
        }

        binding.listButtonAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
        }
    }

    private fun loadData() = thread {
        val listItems =
            ListItemDatabase.open(requireContext()).listItems.getAll().map { entity ->
                ListItem(
                    entity.id,
                    entity.name,
                    resources.getIdentifier(
                        entity.photo,
                        "drawable",
                        requireContext().packageName
                    )
                )
            }
        adapter.replace(listItems)
    }


    override fun onStart() {
        super.onStart()
        //TODO add something?
        adapter.sort()
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
