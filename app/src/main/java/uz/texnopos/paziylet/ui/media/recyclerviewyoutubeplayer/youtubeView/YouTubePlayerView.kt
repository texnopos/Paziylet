package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView

import android.content.Context
import android.graphics.PorterDuff
import android.media.AudioManager
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.fragment.YouTubeBaseFragment
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.fragment.YouTubeFragment
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.fragment.YouTubeWebViewFragment
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.listener.YouTubeEventListener
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.models.ImageLoader
import com.example.recyclerviewyoutubeplayer.youtubeView.models.YouTubePlayerType
import com.squareup.picasso.Picasso
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.util.ServiceUtil
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.util.`$Precondition$Check`


class YouTubePlayerView : FrameLayout {
     lateinit var youtubePlayerFragment: YouTubeBaseFragment

    lateinit var playIcon: ImageView
    @YouTubePlayerType
    private var playerType: Int = 0

    private var videoId: String? = null
    private var listener: YouTubeEventListener? = null
    private val fragment: Fragment? = null

    private var key: String? = null
    private var playerContainer: FrameLayout? = null
    private var thumbnailImageView: ImageView? = null
    private var webViewUrl: String? = null
    private var imageLoader: ImageLoader? = null
    private var rv: RelativeLayout? = null
    private var fullscreen: ImageView? = null
    private var mute: ImageView? = null
    private var time: Int = 0
    private var activity: AppCompatActivity? = null
    var muted = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val newWidth: Int = measuredWidth
        val newHeight: Int = (newWidth * ASPECT_RATIO).toInt()
        setMeasuredDimension(newWidth, newHeight)
        if (playerContainer != null && playerContainer!!.measuredHeight != newHeight) {
            val layoutParams = playerContainer!!.layoutParams
            layoutParams.height = newHeight
            playerContainer!!.layoutParams = layoutParams

            val url = "https://img.youtube.com/vi/$videoId/0.jpg"
            Picasso.get().load(url).resize(measuredWidth, measuredHeight).into(thumbnailImageView)

        } else {
            val url = "https://img.youtube.com/vi/$videoId/0.jpg"
            Picasso.get().load(url).resize(measuredWidth, measuredHeight).into(thumbnailImageView)
        }

    }

    @MainThread
    fun initPlayer(
        apiKey: String,
        videoId: String, @YouTubePlayerType playerType: Int,
        listener: YouTubeEventListener?,
        fragment: AppCompatActivity,
        imageLoader: ImageLoader?,
        rv: RelativeLayout,
        fullscreen: ImageView,
        time: Int,
        mute: ImageView
    ) {
        `$Precondition$Check`.checkArgument(!TextUtils.isEmpty(apiKey), "apiKey cannot be null")
        `$Precondition$Check`.checkArgument(!TextUtils.isEmpty(videoId), "videoId cannot be null")
        `$Precondition$Check`.checkArgument(true, "Fragment cannot be null")

        this.key = apiKey
        this.videoId = videoId
        this.playerType = playerType
        this.rv = rv
        this.fullscreen = fullscreen
        this.listener = listener
        this.time = time
        this.activity = fragment
        this.imageLoader = imageLoader
        this.mute = mute
    }


    private fun init(context: Context) {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.video_container, this, false)
        this.addView(itemView)
        playerContainer = itemView.findViewById(R.id.youtubeFragmentContainer)
        playerContainer!!.id = 0
        thumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image)
        playIcon = itemView.findViewById(R.id.play_btn)

        val progressBar = itemView.findViewById<ProgressBar>(R.id.recycler_progressbar)
        // For else case there is a layout defined for v21 and above
        if (progressBar != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.resources.getColor(R.color.white, null)
            } else {
                context.resources.getColor(R.color.white)
            }
            progressBar.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }

        setListeners()
    }

    private fun setListeners() {
        val onClickListener = OnClickListener { handleBindPlayer() }

        //        thumbnailImageView.setOnClickListener(onClickListener);
        playIcon.setOnClickListener(onClickListener)
        playIcon.visibility = View.INVISIBLE

    }

    private fun handleBindPlayer() {
        when (playerType) {
            YouTubePlayerType.WEB_VIEW -> attachPlayer(false)
            YouTubePlayerType.STRICT_NATIVE -> bindPlayer(false)
            YouTubePlayerType.AUTO, YouTubePlayerType.INVALID_VIEW -> bindPlayer(true)
            else -> bindPlayer(true)
        }
    }

    private fun bindPlayer(auto: Boolean) {
        if (!ServiceUtil.isYouTubeServiceAvailable(context)) {
            if (!auto && listener != null) {
                listener!!.onNativeNotSupported()
            } else {
                attachPlayer(false)
            }
        } else {
            attachPlayer(true)
        }
    }

    private fun attachPlayer(isNative: Boolean) {
        if (playerContainer!!.id != R.id.youtubeFragmentContainer) {

            val currentYouTubeFragment = removeCurrentYouTubeFragment()
            playerContainer!!.id = R.id.youtubeFragmentContainer
            youtubePlayerFragment = if (isNative) {
                YouTubeFragment.newInstance(key!!, videoId!!, time)

            } else {
                val webViewFragment = YouTubeWebViewFragment.newInstance(webViewUrl!!, videoId!!)
                if (currentYouTubeFragment is YouTubeWebViewFragment) {
                    webViewFragment.setWebView(currentYouTubeFragment.removeWebView())
                }
                webViewFragment
            }
            youtubePlayerFragment.setYouTubeEventListener(listener)
            youtubePlayerFragment.setFullScreenVisibility(rv as View, fullscreen)


            if (fragment != null)
                this.fragment.childFragmentManager.beginTransaction().add(
                    R.id.youtubeFragmentContainer,
                    youtubePlayerFragment as Fragment,
                    TAG
                )
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit()
            else
                this.activity!!.supportFragmentManager.beginTransaction().add(
                    R.id.youtubeFragmentContainer,
                    youtubePlayerFragment as Fragment,
                    TAG
                )
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit()
        }
    }

    private fun removeCurrentYouTubeFragment(): YouTubeBaseFragment? {
        val fragmentManager: FragmentManager = activity!!.supportFragmentManager
        val youTubeFragment = fragmentManager.findFragmentByTag(TAG)
        var youTubeBaseFragment: YouTubeBaseFragment? = null
        if (youTubeFragment is YouTubeBaseFragment) {
            youTubeBaseFragment = youTubeFragment

            assert(listener != null)
            if (youTubeBaseFragment.getPlayer() != null)
                listener!!.onStop(
                    youTubeBaseFragment.getPlayer()!!.currentTimeMillis,
                    youTubeBaseFragment.getPlayer()!!.durationMillis
                )

            val fragmentView = youTubeFragment.view
            val parentView = fragmentView?.parent
            youTubeBaseFragment.release()
            fragmentManager.beginTransaction().remove(youTubeFragment).commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
            if (parentView is View && (parentView as View).id == R.id.youtubeFragmentContainer) {
                (parentView as View).id = 0
            }
        }

        return youTubeBaseFragment
    }

    private fun unbindPlayer() {
        if (playerContainer!!.id == R.id.youtubeFragmentContainer) {
            removeCurrentYouTubeFragment()
        }
    }

    override fun onDetachedFromWindow() {
        unbindPlayer()
        super.onDetachedFromWindow()
    }

    fun click(muted: Boolean) {
        setVolume(muted)
        playIcon.performClick()
    }

    private fun setVolume(muted: Boolean) {
        val audioManager = activity!!.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, muted)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun toggleVolume(): Boolean {
        val audioManager = activity!!.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val data = audioManager.isStreamMute(AudioManager.STREAM_MUSIC)

        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, !audioManager.isStreamMute(AudioManager.STREAM_MUSIC))

        return data
    }

    companion object {

        val TAG = "YouTubeFragmentTAG"
        private val ASPECT_RATIO = 0.5625 //aspect ratio of player 9:16(height/width)
    }
}
