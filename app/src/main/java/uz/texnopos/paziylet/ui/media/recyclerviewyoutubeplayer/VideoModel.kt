package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer

data class VideoModel(
    var category: String="",
    var createdAt:Long=0L,
    var description:String="",
    var id: String="",
    var title: String="",
    var url: String="",
    var seekTime: Int = 0
)