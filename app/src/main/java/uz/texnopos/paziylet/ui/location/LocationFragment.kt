package uz.texnopos.paziylet.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.Resource
import uz.texnopos.paziylet.ui.compass.CompassFragment

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
    private val mutableLocation = MutableLiveData<Resource<Location>>()
    val location: LiveData<Resource<Location>> get() = mutableLocation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mutableLocation.value = Resource.loading()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        location.observe(viewLifecycleOwner, {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        })
    }

    private fun checkGpsStatus() {
        val settingsClient: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationSettingsRequest: LocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(context as Activity) {

                }
                .addOnFailureListener(requireActivity()) { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            try {
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(requireActivity(), 101)
                            } catch (sie: IntentSender.SendIntentException) {
                            }
                    }
                }
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage(getString(R.string.permission_is_required))
            setTitle(getString(R.string.permission_required_title))
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(getString(R.string.go_to_settings)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }.create().show()
    }

    private fun checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    CompassFragment.FINE_LOCATION
                )
            } else {
                checkGpsStatus()
            }
        } else checkGpsStatus()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck() {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showDialog()
            } else {
                checkGpsStatus()
            }
        }
        when (requestCode) {
            CompassFragment.FINE_LOCATION -> innerCheck()
        }
    }

    override fun onStart() {
        super.onStart()
        checkForPermissions()
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                mutableLocation.postValue(Resource.success(location))
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