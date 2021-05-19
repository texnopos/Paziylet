package uz.texnopos.paziylet.data.model

data class Question(
    val categoryId: String="",
    val id: String="",
    val rejected:Boolean=false,
    val createdAt:Long=0L,
    val juwap: String="",
    val soraw: String="",
)
