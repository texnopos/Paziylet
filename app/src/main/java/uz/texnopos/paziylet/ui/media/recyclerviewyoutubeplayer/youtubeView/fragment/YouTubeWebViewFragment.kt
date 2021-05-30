package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.listener.YouTubeEventListener
import com.google.android.youtube.player.YouTubePlayer
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.webview.YouTubePlayerWebView


class YouTubeWebViewFragment : Fragment(), YouTubeEventListener, YouTubeBaseFragment {
    override fun setFullScreenVisibility(v: View, fullscreen: ImageView?) {
    }

    private var youTubePlayerWebView: YouTubePlayerWebView? = null

    private var youTubeEventListener: YouTubeEventListener? = null

    override fun setYouTubeEventListener(listener: YouTubeEventListener?) {
        youTubeEventListener = listener
    }



    override fun release() {
        if (youTubePlayerWebView != null) {
            youTubePlayerWebView!!.stopPlayer()
        }
    }

    override fun getPlayer(): YouTubePlayer? {
        return null
    }

    fun setWebView(webView: YouTubePlayerWebView) {
        youTubePlayerWebView = webView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val arguments = arguments
        val webViewUrl = arguments?.getString(WEB_VIEW_URL)
        if (TextUtils.isEmpty(webViewUrl)) {
            throw IllegalStateException("webViewUrl cannot be null")
        }
        return bindYoutubePlayerWebView(inflater, container, webViewUrl!!)
    }

    private fun bindYoutubePlayerWebView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        webViewUrl: String
    ): YouTubePlayerWebView {
        if (youTubePlayerWebView == null) {
            youTubePlayerWebView =
                inflater.inflate(R.layout.youtube_player_web_view, container, false) as YouTubePlayerWebView
            youTubePlayerWebView!!.initialize(webViewUrl)
            youTubePlayerWebView!!.setYouTubeListener(this)
            setWebViewProps()
            //on ready event will be fired by default
        } else {
            removeWebView()
            youTubePlayerWebView!!.initialize(webViewUrl)
            youTubePlayerWebView!!.setYouTubeListener(this)
            setWebViewProps()
            youTubePlayerWebView!!.onReadyPlayer()
        }
        return youTubePlayerWebView as YouTubePlayerWebView
    }

    private fun setWebViewProps() {
        youTubePlayerWebView!!.resetTime()
    }

    fun removeWebView(): YouTubePlayerWebView {
        youTubePlayerWebView!!.stopPlayer()
        return youTubePlayerWebView as YouTubePlayerWebView
    }

    override fun onReady() {
        val arguments = arguments
        val videoId = arguments?.getString(VIDEO_ID)
        youTubePlayerWebView!!.loadVideo(videoId)
        if (youTubeEventListener != null) {
            youTubeEventListener!!.onReady()
        }
    }

    override fun onPlay(currentTime: Int) {
        if (youTubeEventListener != null) {
            youTubeEventListener!!.onPlay(currentTime)
        }
    }

    override fun onPause(currentTime: Int) {
        if (youTubeEventListener != null) {
            youTubeEventListener!!.onPause(currentTime)
        }
    }

    override fun onStop(currentTime: Int, totalDuration: Int) {
        if (youTubeEventListener != null) {
            youTubeEventListener!!.onStop(currentTime, totalDuration)
        }
    }

    override fun onBuffering(currentTime: Int, isBuffering: Boolean) {
        if (youTubeEventListener != null) {
            youTubeEventListener!!.onBuffering(currentTime, isBuffering)
        }
    }

    override fun onSeekTo(currentTime: Int, newPositionMillis: Int) {
        if (youTubeEventListener != null) {
            youTubeEventListener!!.onSeekTo(currentTime, newPositionMillis)
        }
    }

    override fun onInitializationFailure(error: String) {
        if (youTubeEventListener != null) {
            youTubeEventListener!!.onInitializationFailure(error)
        }
    }

    override fun onNativeNotSupported() {
        if (youTubeEventListener != null) {
            youTubeEventListener!!.onNativeNotSupported()
        }
    }

    override fun onCued() {
        if (youTubeEventListener != null) {
            youTubeEventListener!!.onCued()
        }
    }

    companion object {

        private val WEB_VIEW_URL = "webViewUrl"
        private val VIDEO_ID = "videoId"

        fun newInstance(webViewUrl: String, videoId: String): YouTubeWebViewFragment {
            val fragment = YouTubeWebViewFragment()
            val bundle = Bundle()
            bundle.putString(WEB_VIEW_URL, webViewUrl)
            bundle.putString(VIDEO_ID, videoId)
            fragment.arguments = bundle
            return fragment
        }
    }
}
