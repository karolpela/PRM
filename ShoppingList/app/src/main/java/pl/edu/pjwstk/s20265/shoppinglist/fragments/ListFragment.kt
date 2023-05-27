package pl.edu.pjwstk.s20265.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjwstk.s20265.shoppinglist.Navigable
import pl.edu.pjwstk.s20265.shoppinglist.R
import pl.edu.pjwstk.s20265.shoppinglist.adapters.ListItemsAdapter
import pl.edu.pjwstk.s20265.shoppinglist.data.DataSource
import pl.edu.pjwstk.s20265.shoppinglist.databinding.FragmentListBinding

class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    lateinit var adapter: ListItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onResume() {
        super.onResume()
        adapter.sort()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter =
            ListItemsAdapter().apply {
                replace(DataSource.listItems)
                sort()
            }

        binding.listItems.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this.context)
        }

        binding.listButtonAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
        }

        binding.listTotal.text =
            getString(
                R.string.list_total,
                DataSource.priceFormat.format(DataSource.getTotalPrice())
            )
    }

    override fun onStart() {
        super.onStart()
        adapter.replace(DataSource.listItems)
        adapter.sort()
    }
}
