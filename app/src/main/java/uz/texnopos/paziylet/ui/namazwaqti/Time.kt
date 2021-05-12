package uz.texnopos.paziylet.ui.namazwaqti

import com.google.gson.annotations.SerializedName

data class Time(
    @SerializedName("fajr")
    var tan:String,
    @SerializedName("sunrise")
    var quyash:String,
    @SerializedName("dhuhr")
    var pesin:String,
    var asr:String,
    @SerializedName("isha")
    var sham:String,
    @SerializedName("midnight")
    var quftan:String
)