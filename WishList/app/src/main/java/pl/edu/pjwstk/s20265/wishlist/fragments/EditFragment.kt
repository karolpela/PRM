package pl.edu.pjwstk.s20265.wishlist.fragments

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import pl.edu.pjwstk.s20265.wishlist.R
import pl.edu.pjwstk.s20265.wishlist.data.ListItemDatabase
import pl.edu.pjwstk.s20265.wishlist.data.model.ListItemEntity
import pl.edu.pjwstk.s20265.wishlist.databinding.FragmentEditBinding
import java.time.LocalDateTime
import kotlin.concurrent.thread

private const val ARG_LIST_ITEM_ID = "list_item_id"

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var database: ListItemDatabase
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var listItem: ListItemEntity? = null
    private var photoUri: Uri? = null

    private val onTakePhoto: (Boolean) -> Unit = { keep: Boolean ->
        if (keep) {
            loadPhoto()
        } else {
            photoUri?.let {
                requireContext().contentResolver.delete(it, null, null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture(),
            onTakePhoto
        )
        database = ListItemDatabase.open(requireContext().applicationContext)
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
        setupMenu()

        val listItemId = arguments?.getLong(ARG_LIST_ITEM_ID)
        thread {
            listItem = listItemId?.let {
                database.listItems.getListItem(it).also {
                    photoUri = Uri.parse(it.photoUriString)
                    binding.editItemName.setText(it.name)
                }
            }
            loadPhoto()
        }

        binding.editItemPhoto.setOnClickListener { takePhoto() }

        binding.editButtonSave.setOnClickListener {
            val listItem = listItem?.let {
                it.copy(
                    name = binding.editItemName.text.toString(),
                    photoUriString = photoUri.toString(),
                    addedOn = it.addedOn
                )
            } ?: ListItemEntity(
                name = binding.editItemName.text.toString(),
                photoUriString = photoUri.toString(), // keep in mind this writes "null" if Uri is null
                addedOn = LocalDateTime.now()
            )
            this.listItem = listItem // not used after saving, but just in case

            thread {
                database.listItems.addListItem(listItem) // updates if item exists
            }
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.edit_fragment_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId) {
                        R.id.edit_action_delete -> {
                            listItem?.let { thread { database.listItems.deleteListItem(it.id) } }
                            parentFragmentManager.popBackStack()
                            true
                        }

                        else -> {
                            false
                        }
                    }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }

    private fun takePhoto() {
        val newPhotoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val cv = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "picture.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        photoUri = requireContext().contentResolver.insert(newPhotoUri, cv)
        cameraLauncher.launch(photoUri)
    }

    private fun loadPhoto() {
        photoUri?.let {
            if (it.toString() != "null") {
                requireContext().contentResolver.openInputStream(it)?.use {
                    BitmapFactory.decodeStream(it)?.let {
                        binding.editItemPhoto.photo = it
                        return
                    }
                }
            }
        }
    }


}
