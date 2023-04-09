package pl.edu.pjwstk.s20265.shoppinglist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjwstk.s20265.shoppinglist.databinding.FragmentListBinding
import pl.edu.pjwstk.s20265.shoppinglist.databinding.ListItemBinding

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ListItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListItemsAdapter().apply {
            replace(DataSource.listItems)
        }

        binding.listItems.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this.context)
        }

        binding.listButtonAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
        }
    }

    override fun onStart() {
        super.onStart()
        // TODO maybe check if initialized?
        // Seems to not crash anyway...
        adapter.replace(DataSource.listItems)
    }
}