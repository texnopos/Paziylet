package uz.texnopos.paziylet.ui.compass

import android.content.Context
import android.hardware.*
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_compass.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.ResourceState
import uz.texnopos.paziylet.core.extentions.onClick
import uz.texnopos.paziylet.core.extentions.visibility
import uz.texnopos.paziylet.ui.location.LocationFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CompassFragment : LocationFragment(R.layout.fragment_compass) {
    companion object {
        const val QIBLA_LATITUDE = 21.38908
        const val QIBLA_LONGITUDE = 39.85791
        const val FINE_LOCATION = 101
    }

    var currentDegree: Float = 0f
    var currentNeedleDegree: Float = 0f
    private lateinit var sensor: Sensor
    lateinit var userLocation: Location
    lateinit var needleAnimation: RotateAnimation
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvDate.text = getCurrentDateAndTime().toString()
        navController = Navigation.findNavController(view)
        location.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> {
                    progressBar.visibility(true)
                }
                ResourceState.SUCCESS -> {
                    it.data?.let { location ->
                        progressBar.visibility(false)
                        initQiblaDirection(location.latitude, location.longitude)
                        getCountryName(
                            { county->
                                tvRegion.text = county
                            },
                            requireContext()
                        , location.latitude, location.longitude

                        )
                    }
                }
                else -> {}
            }
        })
        btnBackCompass.onClick {
            navController.popBackStack()
        }
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

    private fun getCountryName(
        onSuccess: (address: String) -> Unit,
        context: Context?,
        latitude: Double,
        longitude: Double
    ){
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latitude, longitude, 1)
        onSuccess.invoke("${addresses[0].locality}, ${addresses[0].countryName}")
    }

    private fun getCurrentDateAndTime(): Any {
        val c = Calendar.getInstance().time
        val simpleDateFormat =
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
                .toString()
        return simpleDateFormat.format(c)
    }
}
