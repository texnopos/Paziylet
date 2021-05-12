package uz.texnopos.paziylet.ui.namazwaqti

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NetworkHelper(private val apiInterface: ApiInterface) {

    private val year=Calendar.getInstance().get(Calendar.YEAR)
    //private val timezone=Calendar.getInstance().timeZone.getOffset()

    fun getTimes(onSuccess:(time:Time)->Unit,onFailure:(msg:String?)->Unit){
        val call:Call<Time> = apiInterface.getData(year,42.634823,58.930749,5)
        call.enqueue(object :Callback<Time>{
            override fun onResponse(call: Call<Time>?, response: Response<Time>?) {
                response?.let {
                    onSuccess.invoke(response.body())
                }
            }
            override fun onFailure(call: Call<Time>?, t: Throwable?) {
                onFailure.invoke(t?.localizedMessage)
            }

        })
    }
}