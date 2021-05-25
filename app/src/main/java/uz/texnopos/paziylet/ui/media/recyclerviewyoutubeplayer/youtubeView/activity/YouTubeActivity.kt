package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.MainThread
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.listener.YouTubeEventListener
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.models.PlayerStateList
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.util.ServiceUtil
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

import android.view.View.GONE
import android.view.View.VISIBLE
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.util.`$Precondition$Check`
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.webview.YouTubePlayerWebView

class YouTubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    @PlayerStateList.PlayerState
    private var playerState = PlayerStateList.NONE

    private var youTubePlayer: YouTubePlayer? = null

    internal var seekTime = 0

    private val videoId: String?
        get() {
            val extras = if (null != intent) intent.extras else null
            return extras?.getString(ARG_VIDEO_ID)
        }

    private val apiKey: String?
        get() {
            val extras = if (null != intent) intent.extras else null
            return extras?.getString(ARG_API_KEY)
        }

    private val webUrl: String?
        get() {
            val extras = if (null != intent) intent.extras else null
            return extras?.getString(ARG_WEB_URL)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullscreen()
        setContentView(R.layout.youtube_player_view)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player)

        val videoId = videoId
        val apiKey = apiKey
        seekTime = getSeekTime()

        /*
         * In case videoId or apiKey is null, throw IllegalStateException as apiKey and videoId is mandatory to run
         * youtube activity.
         */
        `$Precondition$Check`.checkArgument(!TextUtils.isEmpty(videoId), " videoId cannot be null")
        `$Precondition$Check`.checkArgument(!TextUtils.isEmpty(apiKey), " apiKey cannot be null")

        /*
         * In case of YouTube Service not available, fallback to WebView implementation.
         */
        if (ServiceUtil.isYouTubeServiceAvailable(this)) {
            youTubePlayerView.initialize(apiKey, this)
        } else {
            val webViewUrl = webUrl
            if (!TextUtils.isEmpty(webViewUrl)) {
                youTubePlayerView.visibility = GONE
                if (videoId != null) {
                    if (webViewUrl != null) {
                        handleWebViewPlayer(videoId, webViewUrl)
                    }
                }
            } else {
                Log.d(TAG, "Web Url is Null")
                finish()
            }
        }
    }

    private fun getSeekTime(): Int {
        val extras = if (null != intent) intent.extras else null
        return extras?.getInt("seeked_time")!!
    }

    private fun setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun handleWebViewPlayer(videoId: String, webViewUrl: String) {
        //initialize youtube player webview
        val youTubePlayerWebView = YouTubePlayerWebView(this)
        val layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        youTubePlayerWebView.layoutParams = layoutParams

        //initialize progressbar and attach it to the view.
        val progressBar = initializeProgressBar()
        handleProgressBar(progressBar, true)

        val progressBarParams =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        progressBarParams.gravity = Gravity.CENTER
        youTubePlayerWebView.setBackgroundColor(resources.getColor(R.color.black))
        youTubePlayerWebView.initialize(webViewUrl)

        val youTubeEventListener = getYoutubeEventListener(youTubePlayerWebView, progressBar, videoId)
        youTubePlayerWebView.setYouTubeListener(youTubeEventListener)

        addContentView(youTubePlayerWebView, layoutParams)
        addContentView(progressBar, progressBarParams)
    }

    private fun initializeProgressBar(): ProgressBar {
        return ProgressBar(this)
    }

    private fun handleProgressBar(progressBar: ProgressBar, show: Boolean) {
        progressBar.visibility = if (show) VISIBLE else GONE
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, restored: Boolean) {
        youTubePlayer = player
        youTubePlayer!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
        youTubePlayer!!.setShowFullscreenButton(false)
        youTubePlayer!!.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION)
        youTubePlayer!!.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI)

        youTubePlayer!!.setFullscreen(true)


        youTubePlayer!!.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
            override fun onPlaying() {
                if (youTubePlayer != null && PlayerStateList.PLAYING != playerState) {

                    playerState = PlayerStateList.PLAYING
                }
                if (seekTime != 0) {
                    assert(youTubePlayer != null)
                    youTubePlayer!!.seekToMillis(seekTime)
                    seekTime = 0
                    youTubePlayer!!.play()
                }
            }

            override fun onPaused() {
                handleOnPauseEvent()
            }

            override fun onStopped() {
                handleStopEvent()
            }

            override fun onBuffering(isBuffering: Boolean) {
                //intentionally left blank
            }

            override fun onSeekTo(newPositionMillis: Int) {
                handleOnPauseEvent()
            }
        })

        youTubePlayer!!.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
            override fun onLoading() {
                //intentionally left blank
            }

            override fun onLoaded(s: String) {
                //intentionally left blank
            }

            override fun onAdStarted() {
                //intentionally left blank
            }

            override fun onVideoStarted() {
                //intentionally left blank
            }

            override fun onVideoEnded() {
                handleStopEvent()
            }

            override fun onError(errorReason: YouTubePlayer.ErrorReason) {
                //intentionally left blank
            }
        })

        player.setOnFullscreenListener { handleStopEvent() }

        if (!restored) {
            youTubePlayer!!.loadVideo(videoId)

        }
    }

    private fun handleOnPauseEvent() {
        if (youTubePlayer != null && PlayerStateList.PLAYING == playerState || PlayerStateList.BUFFERING == playerState) {
            playerState = PlayerStateList.PAUSED
        }
    }

    private fun handleStopEvent() {
        if (youTubePlayer != null && PlayerStateList.PLAYING == playerState
            || PlayerStateList.BUFFERING == playerState || PlayerStateList.PAUSED == playerState
        ) {
            playerState = PlayerStateList.STOPPED
        }
    }

    private fun getYoutubeEventListener(
        youTubePlayerWebView: YouTubePlayerWebView,
        progressBar: ProgressBar,
        videoId: String
    ): YouTubeEventListener {
        return object : YouTubeEventListener {
            @MainThread
            override fun onReady() {
                youTubePlayerWebView.loadVideo(videoId)
                handleProgressBar(progressBar, false)
            }

            override fun onCued() {
                //intentionally left blank
            }

            @MainThread
            override fun onPlay(currentTime: Int) {
                seekTime = currentTime
                handleProgressBar(progressBar, false)
            }

            @MainThread
            override fun onPause(currentTime: Int) {
                //intentionally left blank
            }

            @MainThread
            override fun onStop(currentTime: Int, totalDuration: Int) {
                //intentionally left blank
            }

            @MainThread
            override fun onBuffering(currentTime: Int, isBuffering: Boolean) {
                //intentionally left blank
            }

            @MainThread
            override fun onSeekTo(currentTime: Int, newPositionMillis: Int) {
                //intentionally left blank
            }

            @MainThread
            override fun onInitializationFailure(error: String) {
                //intentionally left blank
            }

            @MainThread
            override fun onNativeNotSupported() {
                //intentionally left blank
            }
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, result: YouTubeInitializationResult) {
        this.youTubePlayer = null
    }

    public override fun onStop() {
        super.onStop()
        handleStopEvent()
    }

    override fun onBackPressed() {
        assert(youTubePlayer != null)
        youTubePlayer!!.setFullscreen(false)

        val i = Intent()
        i.putExtra("seeked_time", youTubePlayer!!.currentTimeMillis)
        setResult(Activity.RESULT_OK, i)
        finish()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (youTubePlayer != null) {
            youTubePlayer!!.release()
        }
    }

    companion object {
        /*
     * Pass video id as extras
     */
        val ARG_VIDEO_ID = "videoId"

        /**
         * Pass api key as extras
         */
        val ARG_API_KEY = "apiKey"

        /**
         * Pass web-url as extras
         */
        val ARG_WEB_URL = "webUrl"

        private val TAG = "YoutubeActivity"
    }
}
