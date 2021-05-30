package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.util

object `$Precondition$Check` {

    fun checkArgument(expression: Boolean, errorMessage: Any) {
        if (!expression) {
            throw IllegalArgumentException(errorMessage.toString())
        }
    }
}
