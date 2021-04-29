package uz.texnopos.paziylet.ui.compass

import android.content.Context.SENSOR_SERVICE
import android.hardware.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.format.Time
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.compass_fragment.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import uz.texnopos.paziylet.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class CompassFragment: Fragment(R.layout.compass_fragment) , SensorEventListener{
    private lateinit  var sensorManager : SensorManager


    private lateinit var sensor : Sensor
    private lateinit var magnetometerSensor: Sensor
    private var mGravity = FloatArray(3)
    private var mGeometric = FloatArray(3)
    private var azimuth = 0f
    private var currentAzimuth : Float = 0f


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            getDate()
    }

    private fun getDate(){
        val time = Time(Time.getCurrentTimezone())
        time.setToNow()
        tvDate.text = time.format("%Y.%m.%d")
    }


    @OptIn(InternalCoroutinesApi::class)
    override fun onSensorChanged(event: SensorEvent?) {
        val alpha= 0.97f
        synchronized(this){
            if (event != null) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER){
                    mGravity[0] = alpha*mGravity[0]+(1-alpha)*event.values[0]
                    mGravity[1] = alpha*mGravity[1]+(1-alpha)*event.values[1]
                    mGravity[2] = alpha*mGravity[2]+(1-alpha)*event.values[2]
                }

                if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
                    mGeometric[0] = alpha*mGeometric[0]+(1-alpha)*event.values[0]
                    mGeometric[1] = alpha*mGeometric[1]+(1-alpha)*event.values[1]
                    mGeometric[2] = alpha*mGeometric[2]+(1-alpha)*event.values[2]
                }

                val R = FloatArray(9)
                val I = FloatArray(9)
                val success : Boolean = SensorManager.getRotationMatrix(R, I, mGravity, mGeometric)
                if (success){
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)
                    azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    azimuth = (azimuth+360)%360
                    val anim = RotateAnimation(-currentAzimuth, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                    currentAzimuth = azimuth
                    anim.duration = 500
                    anim.repeatCount = 0
                    anim.fillAfter = true
                    ivCompass.startAnimation(anim)
                    val x = azimuth
                    tvDegree.text = "$x degrees"
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


//    override fun onSensorChanged(event: SensorEvent?) {
//        val degree = event?.values!![0].roundToInt()
//        val animation = RotateAnimation(0F, (-degree).toFloat(), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
//        animation.duration = 500
//        animation.fillAfter = true
//        ivStrelka.animation = animation
//        currentDegree=-degree.toFloat()
//    }
//
//    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//    }

}