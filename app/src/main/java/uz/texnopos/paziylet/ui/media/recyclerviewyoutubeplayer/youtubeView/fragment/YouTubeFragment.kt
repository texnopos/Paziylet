package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.annotation.MainThread
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.activity.YouTubeActivity
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.listener.YouTubeEventListener
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.models.PlayerStateList
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import android.app.Activity.RESULT_OK
import android.app.PendingIntent.getActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.util.`$Precondition$Check`


class YouTubeFragment : YouTubePlayerSupportFragment(), YouTubePlayer.OnInitializedListener,
    YouTubeBaseFragment {
    override fun setFullScreenVisibility(v: View, fullscreen: ImageView?) {
        fullscreenView = v
        this.fullscreen = fullscreen!!
    }

    @PlayerStateList.PlayerState
    private var playerState = PlayerStateList.NONE
    private var listener: YouTubeEventListener? = null
    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var fullscreenView: View
    private lateinit var fullscreen: View
    private var seekedTime = 0

    override fun setYouTubeEventListener(listener: YouTubeEventListener?) {
        this.listener = listener
    }


    override fun onSaveInstanceState(outState: Bundle) {
        /* release youTubePlayer when home button pressed. */
        release()
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if (null == youTubePlayer) {
            if (null != arguments) {

                initialize(arguments!!.getString(ARG_API_KEY), this)
            }
        }
    }


    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, restored: Boolean) {

        fullscreenView.setOnClickListener { v -> openFullScreenAvrivity(v) }

        youTubePlayer = player
        youTubePlayer!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
        youTubePlayer!!.setShowFullscreenButton(false)

        assert(getArguments() != null)
        if (seekedTime == 0)
            seekedTime = getArguments()!!.getInt("seeked_time", 0)
        //        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);

        if (listener != null) {
            listener!!.onReady()
        }
        youTubePlayer!!.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
            override fun onPlaying() {
                if (listener != null && youTubePlayer != null && PlayerStateList.PLAYING != playerState) {
                    playerState = PlayerStateList.PLAYING
                    listener!!.onPlay(youTubePlayer!!.currentTimeMillis)
                }
                if (seekedTime != 0) {
                    assert(youTubePlayer != null)
                    youTubePlayer!!.seekToMillis(seekedTime)

                    seekedTime = 0
                }
            }

            override fun onPaused() {
                handleOnPauseEvent()
            }

            override fun onStopped() {
                //since these are player stop events in case of any player error so pause event not stop.
                handleStopEvent()
            }

            override fun onBuffering(isBuffering: Boolean) {
                if (listener != null && youTubePlayer != null) {
                    listener!!.onBuffering(youTubePlayer!!.currentTimeMillis, isBuffering)
                }
            }

            override fun onSeekTo(newPositionMillis: Int) {
                //just to mimick the pause event before play on seek event.
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

        if (!restored) {
            //do any work here to cue video, play video, etc.
            val arguments = getArguments()
            val videoId = if (arguments != null) arguments!!.getString(ARG_VIDEO_ID) else null
            if (!TextUtils.isEmpty(videoId)) {
                youTubePlayer!!.loadVideo(videoId)
            }
        }
    }

    private fun openFullScreenAvrivity(v: View) {
        assert(getArguments() != null)
        assert(youTubePlayer != null)
        if (getActivity() != null)
            startActivityForResult(
                Intent(getActivity(), YouTubeActivity::class.java)
                    .putExtra(ARG_VIDEO_ID, getArguments()?.getString(ARG_VIDEO_ID))
                    .putExtra(ARG_API_KEY, getArguments()?.getString(ARG_API_KEY))
                    .putExtra("seeked_time", youTubePlayer!!.currentTimeMillis), 1
            )


    }

    private fun handleOnPauseEvent() {
        if (listener != null && youTubePlayer != null && (PlayerStateList.PLAYING == playerState || PlayerStateList.BUFFERING == playerState)) {
            playerState = PlayerStateList.PAUSED
            listener!!.onPause(youTubePlayer!!.currentTimeMillis)
        }
    }

    private fun handleStopEvent() {
        if (listener != null && youTubePlayer != null && (PlayerStateList.PLAYING == playerState
                    || PlayerStateList.BUFFERING == playerState || PlayerStateList.PAUSED == playerState)
        ) {
            playerState = PlayerStateList.STOPPED
            listener!!.onStop(youTubePlayer!!.currentTimeMillis, youTubePlayer!!.durationMillis)
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, result: YouTubeInitializationResult) {
        youTubePlayer = null
        if (listener != null) {
            listener!!.onInitializationFailure(result.name)
        }
    }

    override fun onStop() {
        super.onStop()
        handleStopEvent()
        release()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        release()
    }

    @MainThread
    override fun release() {
        if (youTubePlayer != null) {
            youTubePlayer!!.release()
            youTubePlayer = null
        }
    }

    override fun getPlayer(): YouTubePlayer {
        return youTubePlayer!!
    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            seekedTime = data.getIntExtra("seeked_time", 0)
        }

    }*/

    companion object {

        private val ARG_VIDEO_ID = "videoId"
        private val ARG_API_KEY = "apiKey"

        fun newInstance(apiKey: String, videoId: String, time: Int): YouTubeFragment {
            `$Precondition$Check`.checkArgument(!TextUtils.isEmpty(apiKey), "apiKey cannot be null")
            `$Precondition$Check`.checkArgument(!TextUtils.isEmpty(videoId), "videoId cannot be null")
            val fragment = YouTubeFragment()
            val bundle = Bundle()
            bundle.putString(ARG_VIDEO_ID, videoId)
            bundle.putString(ARG_API_KEY, apiKey)
            bundle.putInt("seeked_time", time)
            fragment.setArguments(bundle)
            return fragment
        }
    }

}
