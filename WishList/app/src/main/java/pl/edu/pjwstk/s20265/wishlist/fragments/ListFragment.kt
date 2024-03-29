package pl.edu.pjwstk.s20265.wishlist.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import pl.edu.pjwstk.s20265.wishlist.Navigable
import pl.edu.pjwstk.s20265.wishlist.adapters.ListItemsAdapter
import pl.edu.pjwstk.s20265.wishlist.data.ListItemDatabase
import pl.edu.pjwstk.s20265.wishlist.databinding.FragmentListBinding
import pl.edu.pjwstk.s20265.wishlist.model.ListItem
import kotlin.concurrent.thread

class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    lateinit var adapter: ListItemsAdapter
    lateinit var database: ListItemDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = ListItemDatabase.open(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
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
        val listItems = database.listItems.getAllSortedByDate().map { entity ->
            ListItem(
                entity.id,
                entity.name,
                Uri.parse(entity.photoUriString),
                entity.note,
                if (entity.latitude != null && entity.longitude != null) LatLng(
                    entity.latitude, entity.longitude
                ) else null,
                entity.locationString,
                entity.addedOn
            )
        }
        requireActivity().runOnUiThread {
            adapter.replace(listItems)
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
