package uz.texnopos.paziylet.data.model

import com.google.gson.annotations.SerializedName

data class PrayerTimeResponse(
    val message: String,
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("praytime")
    val prayTime: PrayTime

)