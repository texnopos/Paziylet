package uz.texnopos.paziylet.data.retrofit

import android.os.Build
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.texnopos.paziylet.data.model.PrayTime
import uz.texnopos.paziylet.data.model.PrayerTimeResponse
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class NetworkHelper(private val apiInterface: ApiInterface) {

    private val year=Calendar.getInstance().get(Calendar.YEAR)
    private var time = SimpleDateFormat("Z", Locale.getDefault()).format(Calendar.getInstance().time)

    private val timezone=time.toInt()/100

    fun getTimes(
        latitude: Double,
        longitude: Double,
        onSuccess: (response: PrayTime) -> Unit,
        onFailure: (msg: String?) -> Unit
    ){
        val call:Call<PrayerTimeResponse> = apiInterface.getData(year, latitude, longitude, timezone)
        call.enqueue(object : Callback<PrayerTimeResponse> {
            override fun onResponse(
                call: Call<PrayerTimeResponse>?,
                response: Response<PrayerTimeResponse>?
            ) {
                response?.let {
                    onSuccess.invoke(response.body().prayTime)
                }
            }

            override fun onFailure(call: Call<PrayerTimeResponse>?, t: Throwable?) {
                onFailure.invoke(t?.localizedMessage)
            }

        })
    }
}