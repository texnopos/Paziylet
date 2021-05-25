package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.fragment


import android.view.View
import android.widget.ImageView
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.listener.YouTubeEventListener
import com.google.android.youtube.player.YouTubePlayer


interface YouTubeBaseFragment {

    fun setYouTubeEventListener(listener: YouTubeEventListener?)
    fun setFullScreenVisibility(v: View, fullscreen: ImageView?)
    fun release()
    fun getPlayer(): YouTubePlayer?
}
