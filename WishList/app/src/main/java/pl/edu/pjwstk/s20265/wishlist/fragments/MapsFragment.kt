package pl.edu.pjwstk.s20265.wishlist.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pl.edu.pjwstk.s20265.wishlist.R

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
    var selectedLocation: LatLng? = null

    @SuppressLint("MissingPermission")
    private val onPermissionsResult = { result: Map<String, Boolean> ->
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        turnOnLocation()
        map.setOnMapClickListener(::onMapClick)

        map.setOnCameraMoveStartedListener {
            view?.parent?.requestDisallowInterceptTouchEvent(true)

        }

        map.setOnCameraIdleListener {
            view?.parent?.requestDisallowInterceptTouchEvent(false)
        }


        selectedLocation?.let {
            onMapClick(it)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))
        }
        if (selectedLocation == null) {
            LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation.addOnSuccessListener {
                it?.let {
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude, it.longitude), 17f
                        )
                    )
                }
            }

        }
    }

    fun onMapClick(latLng: LatLng) {
        if (!this::map.isInitialized) return
        map.clear()
        val marker = MarkerOptions().position(latLng)
        map.addMarker(marker)
        selectedLocation = latLng
    }

    private fun turnOnLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            permissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(), onPermissionsResult
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}