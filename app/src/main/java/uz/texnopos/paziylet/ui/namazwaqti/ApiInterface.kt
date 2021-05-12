package uz.texnopos.paziylet.ui.namazwaqti

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/?")
    fun getData(@Query("year") year:Int,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double,@Query("timezone") timezone:Int)
        : Call<Time>
}