package pl.edu.pjwstk.s20265.wishlist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import pl.edu.pjwstk.s20265.wishlist.fragments.DeleteDialogFragment
import pl.edu.pjwstk.s20265.wishlist.fragments.EditFragment
import pl.edu.pjwstk.s20265.wishlist.fragments.ListFragment
import pl.edu.pjwstk.s20265.wishlist.viewmodel.MainViewModel
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), Navigable, DeleteDialogFragment.DeleteDialogListener {

    private lateinit var listFragment: ListFragment
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name).commit()
    }

    override fun navigate(to: Navigable.Destination, id: Long?) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
            .apply {
                when (to) {
                    Navigable.Destination.List -> {
                        replace(R.id.container, listFragment, listFragment.javaClass.name)
                    }

                    Navigable.Destination.Add -> {
                        addToBackStack(EditFragment::javaClass.name)
                        replace(
                            R.id.container, EditFragment(), EditFragment::javaClass.name
                        )
                    }

                    Navigable.Destination.Edit -> {
                        addToBackStack(EditFragment::javaClass.name)
                        replace(
                            R.id.container, EditFragment::class.java, Bundle().apply {
                                putLong("list_item_id", id ?: -1)
                            }, EditFragment::javaClass.name
                        )
                    }
                }
            }.commit()
    }

    fun showDeleteDialog() {
        DeleteDialogFragment().show(supportFragmentManager, DeleteDialogFragment.TAG)
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val adapter = listFragment.adapter
        adapter.removeItem(adapter.selectedIndex!!).let {
            thread { listFragment.database.listItems.deleteListItem(it.id) }
        }

        Toast.makeText(
            applicationContext, getString(R.string.toast_item_deleted), Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {}
}
