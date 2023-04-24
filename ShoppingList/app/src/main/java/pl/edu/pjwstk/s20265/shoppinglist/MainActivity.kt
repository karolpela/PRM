package pl.edu.pjwstk.s20265.shoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pjwstk.s20265.shoppinglist.fragments.DetailsFragment
import pl.edu.pjwstk.s20265.shoppinglist.fragments.EditFragment
import pl.edu.pjwstk.s20265.shoppinglist.fragments.ListFragment

class MainActivity : AppCompatActivity(), Navigable {

    private lateinit var listFragment: ListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }

    override fun navigate(to: Navigable.Destination, itemIndex: Int) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .apply {
                when (to) {
                    Navigable.Destination.List -> {
                        replace(
                            R.id.container,
                            listFragment,
                            listFragment.javaClass.name
                        )
                    }

                    Navigable.Destination.Add -> {
                        addToBackStack(EditFragment::javaClass.name)
                        replace(
                            R.id.container,
                            EditFragment(itemIndex),
                            EditFragment::javaClass.name
                        )
                    }

                    Navigable.Destination.Edit -> {
                        addToBackStack(EditFragment::javaClass.name)
                        replace(
                            R.id.container,
                            EditFragment(itemIndex),
                            EditFragment::javaClass.name
                        )
                    }

                    Navigable.Destination.Details -> {
                        addToBackStack(DetailsFragment::javaClass.name)
                        replace(
                            R.id.container,
                            DetailsFragment(itemIndex),
                            DetailsFragment::javaClass.name
                        )
                    }
                }
            }.commit()
    }
}