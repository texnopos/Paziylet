package uz.texnopos.paziylet.ui.compass

import android.Manifest
import android.annotation.SuppressLint
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
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.compass_fragment.*
import uz.texnopos.paziylet.R
import java.util.*
import kotlin.math.roundToInt


class CompassFragment: Fragment(R.layout.compass_fragment){

    companion object {
        const val TAG = "MainActivity"
        const val QIBLA_LATITUDE = 21.38908
        const val QIBLA_LONGITUDE = 39.85791
        const val FINE_LOCATION = 101
    }

    var currentDegree: Float = 0f
    var currentNeedleDegree: Float = 0f
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    lateinit var userLocation: Location
    lateinit var needleAnimation: RotateAnimation
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        needleAnimation = RotateAnimation(
                currentNeedleDegree,
                0f,
                Animation.RELATIVE_TO_SELF,
                .5f,
                Animation.RELATIVE_TO_SELF,
                .5f)
        checkForPermissions()
    }

    override fun onStart() {
        super.onStart()
        initLocationListener()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initLocationListener() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            initQiblaDirection(it.latitude, it.longitude)
            tvRegion.text = getCountryName(requireContext(), it.latitude, it.longitude)
        }
        fusedLocationClient.lastLocation.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initQiblaDirection(latitude: Double, longitude: Double) {
        userLocation = Location("User Location")
        userLocation.latitude = latitude
        userLocation.longitude = longitude
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
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
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck() {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showDialog()
            }
        }
        when (requestCode) {
            FINE_LOCATION -> innerCheck()
        }
    }
}