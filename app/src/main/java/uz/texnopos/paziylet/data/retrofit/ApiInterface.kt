package uz.texnopos.paziylet.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import uz.texnopos.paziylet.data.model.PrayTime
import uz.texnopos.paziylet.data.model.PrayerTimeResponse

interface ApiInterface {

    @GET("/sample.php")
    fun getData(@Query("year") year:Int,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double,@Query("timezone") timezone:Int)
        : Call<PrayerTimeResponse>
}