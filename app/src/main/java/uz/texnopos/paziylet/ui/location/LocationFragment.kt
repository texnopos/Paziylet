package uz.texnopos.paziylet.ui.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*

/**
 * LocationFragment abstract class bolıp, basqa Fragmentler bunnan násil alsa boladı.
 * Qálegen fragment "location" LiveData nı observe qılıp tursa boladı
 */

abstract class LocationFragment(layoutResId: Int) : Fragment(layoutResId) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val trackingInterval: Long = 2000
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = trackingInterval
        fastestInterval = trackingInterval / 2
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val mutableLocation = MutableLiveData<Location>()
    val location: LiveData<Location> get() = mutableLocation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        location.observe(viewLifecycleOwner, {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        })
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                mutableLocation.postValue(location)
            }
        }
    }

    override fun onPause() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        super.onPause()
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}