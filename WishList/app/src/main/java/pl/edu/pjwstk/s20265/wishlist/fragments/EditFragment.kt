package pl.edu.pjwstk.s20265.wishlist.fragments

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import pl.edu.pjwstk.s20265.wishlist.data.ListItemDatabase
import pl.edu.pjwstk.s20265.wishlist.data.model.ListItemEntity
import pl.edu.pjwstk.s20265.wishlist.databinding.FragmentEditBinding
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
            ActivityResultContracts.TakePicture(), onTakePhoto
        )
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

        val listItemId = arguments?.getLong(ARG_LIST_ITEM_ID)
        thread {
            listItem = listItemId?.let {
                database.listItems.getListItem(it).also {
                    photoUri = Uri.parse(it.photoUriString)
                }
            }
            loadPhoto()
        }

        binding.editItemPhoto.setOnClickListener { takePhoto(0) }

        binding.editButtonSave.setOnClickListener {
            val listItem = listItem?.copy(
                name = binding.editItemName.text.toString(),
                photoUriString = photoUri.toString()
            ) ?: ListItemEntity(
                name = binding.editItemName.text.toString(),
                photoUriString = photoUri.toString()
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

    private fun takePhoto(listItemId: Long) {
        val newPhotoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val cv = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "picture${listItemId}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        photoUri = requireContext().contentResolver.insert(newPhotoUri, cv)
        cameraLauncher.launch(photoUri)
    }


    private fun loadPhoto() {
        photoUri?.let {
            requireContext().contentResolver.openInputStream(it)?.use {
                BitmapFactory.decodeStream(it)?.let {
                    binding.editItemPhoto.photo = it
                }
            }
        }
    }
}
