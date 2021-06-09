package uz.texnopos.paziylet.ui.media

data class VideoModel(
    var category: String="",
    var createdAt:Long=0L,
    var description:String="",
    var id: String="",
    var title: String="",
    var titleCyr:String="",
    var url: String="",
    var seekTime: Int = 0
)