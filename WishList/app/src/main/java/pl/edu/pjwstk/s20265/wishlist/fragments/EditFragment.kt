package pl.edu.pjwstk.s20265.wishlist.fragments

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import pl.edu.pjwstk.s20265.wishlist.Geofencing
import pl.edu.pjwstk.s20265.wishlist.R
import pl.edu.pjwstk.s20265.wishlist.data.ListItemDatabase
import pl.edu.pjwstk.s20265.wishlist.data.model.ListItemEntity
import pl.edu.pjwstk.s20265.wishlist.databinding.FragmentEditBinding
import java.time.LocalDateTime
import java.util.Calendar
import kotlin.concurrent.thread

private const val ARG_LIST_ITEM_ID = "list_item_id"

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var database: ListItemDatabase
    private lateinit var geocoder: Geocoder
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
        geocoder = Geocoder(requireContext())
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
                    binding.editItemNote.setText(it.note)
                    binding.editMapsFragment.getFragment<MapsFragment>().apply {
                        if (it.latitude != null && it.longitude != null) {
                            selectedLocation = LatLng(it.latitude, it.longitude).also {
                                onMapClick(it)
                            }
                        }
                    }

                }
            }
            loadPhoto()
        }

        binding.editItemPhoto.setOnClickListener { takePhoto() }

        binding.editButtonSave.setOnClickListener {
            val latitude =
                binding.editMapsFragment.getFragment<MapsFragment>().selectedLocation?.latitude
            val longitude =
                binding.editMapsFragment.getFragment<MapsFragment>().selectedLocation?.longitude

            val latLng: LatLng? =
                if (latitude != null && longitude != null) LatLng(latitude, longitude) else null

            val buildItems = { locationString: String? ->
                val listItem = listItem?.let {
                    it.copy(
                        name = binding.editItemName.text.toString(),
                        photoUriString = photoUri.toString(),
                        note = binding.editItemNote.text.toString(),
                        latitude = latitude,
                        longitude = longitude,
                        locationString = locationString,
                        addedOn = it.addedOn
                    )
                } ?: ListItemEntity(
                    name = binding.editItemName.text.toString(),
                    photoUriString = photoUri.toString(), // keep in mind this writes "null" if Uri is null
                    note = binding.editItemNote.text.toString(),
                    latitude = latitude,
                    longitude = longitude,
                    locationString = locationString,
                    addedOn = LocalDateTime.now()
                )
                this.listItem = listItem // not used after saving, but just in case

                thread {
                    val newId = database.listItems.addListItem(listItem) // updates if item exists
                    latLng?.let { Geofencing.createGeofence(requireContext(), it, newId) }
                }
                parentFragmentManager.popBackStack()
            }

            if (latitude != null && longitude != null) {
                locationToString(LatLng(latitude, longitude), buildItems)
            } else {
                buildItems(null)
            }
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
                            LocationServices.getGeofencingClient(requireContext())
                                .removeGeofences(Geofencing.makePendingIntentForAlert(requireContext()))
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
        val contentUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val cv = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "picture_${Calendar.getInstance().time}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        photoUri = requireContext().contentResolver.insert(contentUri, cv)
        cameraLauncher.launch(photoUri)
    }

    private fun locationToString(latLng: LatLng, then: (String?) -> Unit) {
        val afterResult: (List<Address>?) -> Unit = {
            it?.firstOrNull()
                ?.let { address ->
                    "${address.locality ?: ""} ${address.subLocality ?: ""} ${address.thoroughfare ?: ""} ${address.featureName ?: ""}"
                }?.let(then)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1, afterResult)
        } else {
            @Suppress("DEPRECATION")
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).run(afterResult)
        }
    }


    private fun loadPhoto() {
        // Add property text
        binding.editItemPhoto.text = listItem?.note ?: ""
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
