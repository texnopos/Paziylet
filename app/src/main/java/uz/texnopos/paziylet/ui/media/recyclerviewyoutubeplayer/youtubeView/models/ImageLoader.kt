package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.models

import android.widget.ImageView


interface ImageLoader {
    /**
     * Callback to load image (thumbnail view)
     *
     * @param imageView imageview
     * @param url       url of ima
     * ge
     * @param height    height
     * @param width     width
     */
    fun loadImage(imageView: ImageView, url: String, height: Int, width: Int)
}
