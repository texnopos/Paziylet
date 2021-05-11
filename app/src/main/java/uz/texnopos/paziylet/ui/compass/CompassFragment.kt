package uz.texnopos.paziylet.ui.compass

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.*
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.internal.ConnectionCallbacks
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.compass_fragment.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.data.model.location.GpsUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class CompassFragment : Fragment(R.layout.compass_fragment){
    companion object {
        const val QIBLA_LATITUDE = 21.38908
        const val QIBLA_LONGITUDE = 39.85791
        const val FINE_LOCATION = 101
    }
    private var isGPSEnabled = false
    var currentDegree: Float = 0f
    var currentNeedleDegree: Float = 0f
    private lateinit var sensor: Sensor
    lateinit var userLocation: Location
    lateinit var needleAnimation: RotateAnimation
    private val mutableLocation = MutableLiveData<Location>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = trackingInterval
        fastestInterval = trackingInterval / 2
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val trackingInterval: Long = 2000

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkForPermissions()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        GpsUtils(requireContext()).turnGPSOn(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                isGPSEnabled = isGPSEnable
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        initLocationListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                isGPSEnabled = true
                initLocationListener()
            }
        }
    }

    private fun initLocationListener() {
        tvDate.text = getCurrentDateAndTime().toString()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mutableLocation.observe(this, {
            it?.let { location->
                initQiblaDirection(location.latitude, location.longitude)
                tvRegion.text = getCountryName(requireContext(), it.latitude, it.longitude)
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        })
    }

    private fun initQiblaDirection(latitude: Double, longitude: Double) {
        userLocation = Location("User Location")
        userLocation.latitude = latitude
        userLocation.longitude = longitude
        val sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        sensorManager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }

            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                val degree: Float = sensorEvent?.values?.get(0)?.roundToInt()?.toFloat()!!
                var head: Float = sensorEvent.values?.get(0)?.roundToInt()?.toFloat()!!
                val destLocation = Location("Destination Location")
                destLocation.latitude = QIBLA_LATITUDE
                destLocation.longitude = QIBLA_LONGITUDE
                var bearTo = userLocation.bearingTo(destLocation)
                val geoField = GeomagneticField(
                    userLocation.latitude.toFloat(),
                    userLocation.longitude.toFloat(),
                    userLocation.altitude.toFloat(),
                    System.currentTimeMillis()
                )
                head -= geoField.declination
                if (bearTo < 0) {
                    bearTo += 360
                }
                var direction = bearTo - head
                if (direction < 0) {
                    direction += 360
                }
                needleAnimation = RotateAnimation(
                    currentNeedleDegree,
                    direction,
                    Animation.RELATIVE_TO_SELF,
                    .5f,
                    Animation.RELATIVE_TO_SELF,
                    .6f
                )
                needleAnimation.fillAfter = true
                needleAnimation.duration = 500
                ivCompass?.startAnimation(needleAnimation)
                currentNeedleDegree = direction
                currentDegree = -degree
            }
        }, sensor, SensorManager.SENSOR_DELAY_GAME)
    }

    private fun getCountryName(context: Context?, latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latitude, longitude, 1)
        return "${addresses[0].locality} , ${addresses[0].countryName}"
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
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION)
            }
        }
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
                initLocationListener()
            }
        }
        when (requestCode) {
            FINE_LOCATION -> innerCheck()
        }
    }

    private fun getCurrentDateAndTime(): Any {
        val c = Calendar.getInstance().time
        val simpleDateFormat =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
                .toString()
        return simpleDateFormat.format(c)
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
