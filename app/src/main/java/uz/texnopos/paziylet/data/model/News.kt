package uz.texnopos.paziylet.data.model

import com.google.gson.annotations.SerializedName


data class News(
    val category: String = "",
    @SerializedName("created_at")
    val createdAt: Long = 0L,
    val description: String= "",
    val id : String = "",
    val img: String = "",
    val title: String = "",
    var views: Long = 0L,
    val titleCyr:String="",
    val descriptionCyr: String=""
)
