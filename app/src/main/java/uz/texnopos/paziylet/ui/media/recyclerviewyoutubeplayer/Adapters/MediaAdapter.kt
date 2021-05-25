package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.Adapters

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.VideoModel
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.YouTubePlayerView
import uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.listener.YouTubeEventListener
import com.example.recyclerviewyoutubeplayer.youtubeView.models.YouTubePlayerType
import kotlinx.android.synthetic.main.videoitem.view.*
import uz.texnopos.paziylet.R
import uz.texnopos.paziylet.core.extentions.inflate


class MediaAdapter() : RecyclerView.Adapter<MediaAdapter.VideoPlayerViewHolder>() {

    var models:List<VideoModel> = listOf()
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    inner class VideoPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var fullscreen: ImageView = itemView.findViewById(R.id.fullscreen)
        private var mute: ImageView = itemView.findViewById(R.id.mute)
        private var playerView: YouTubePlayerView = itemView.findViewById(R.id.youtube_view)
        private var rv: RelativeLayout = itemView.findViewById(R.id.rv)
        val API_KEY_YOUTUBE = "AIzaSyAVeTsyAjfpfBBbUQq4E7jooWwtV2D_tjE"

        fun populateModel(model: VideoModel) {
            itemView.title.text = model.title
            val listner : YouTubeEventListener =object : YouTubeEventListener {
                override fun onInitializationFailure(error: String) {
                }
                override fun onReady() {
                }
                override fun onPlay(currentTime: Int) {
                }
                override fun onPause(currentTime: Int) {
                }
                override fun onStop(currentTime: Int, totalDuration: Int) {
                }
                override fun onBuffering(currentTime: Int, isBuffering: Boolean) {
                }
                override fun onSeekTo(currentTime: Int, newPositionMillis: Int) {
                }
                override fun onNativeNotSupported() {
                }
                override fun onCued() {
                }
            }
            val videoId=model.url.substringAfterLast('/')
            if (model.url.isNotEmpty()){
                playerView.initPlayer(API_KEY_YOUTUBE,
                    videoId,
                    YouTubePlayerType.AUTO,
                    listner,
                    itemView.context as AppCompatActivity,
                    null,
                    rv,
                    fullscreen,
                    model.seekTime,
                    mute
                )
            }

        }



        init {
            mute.setOnClickListener{
                if (!playerView.toggleVolume())
                    mute.setImageDrawable(ContextCompat.getDrawable( mute.context,R.drawable.ic_volume_off_grey_24dp))
                else
                    mute.setImageDrawable(ContextCompat.getDrawable( mute.context,R.drawable.ic_volume_up_grey_24dp))
            }
        }

        fun performClick(muted : Boolean) {
            playerView.muted=muted
            playerView.post{
                playerView.click(muted)
                if (muted)
                    mute.setImageDrawable(ContextCompat.getDrawable( mute.context,R.drawable.ic_volume_off_grey_24dp))
                else
                    mute.setImageDrawable(ContextCompat.getDrawable( mute.context,R.drawable.ic_volume_up_grey_24dp))

            }


        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): VideoPlayerViewHolder {
        return VideoPlayerViewHolder(parent.inflate(R.layout.videoitem))
    }

    override fun getItemCount(): Int {
        return models.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: VideoPlayerViewHolder, position: Int) {
        holder.populateModel(models[position])
        if (position==0)
        {
            holder.performClick(false)
        }
    }

}
