package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.util

import android.content.Context
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeIntents

object ServiceUtil {

    /**
     * Check if youtube service is available
     *
     * @param context [Context]
     * @return true if present, else false
     */
    fun isYouTubeServiceAvailable(context: Context): Boolean {
        return YouTubeIntents.isYouTubeInstalled(context) || YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(context) == YouTubeInitializationResult.SUCCESS
    }
}
