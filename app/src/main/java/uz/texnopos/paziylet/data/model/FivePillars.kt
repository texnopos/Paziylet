package uz.texnopos.paziylet.data.model

data class FivePillars(
    val content: String = "",
    val contentCyr: String="",
    val createdAt: Long = 0L,
    val id: String = "",
    val image: String = "",
    val title: String = "",
    val titleCyr: String = "",
    var views: Int = 0
)
