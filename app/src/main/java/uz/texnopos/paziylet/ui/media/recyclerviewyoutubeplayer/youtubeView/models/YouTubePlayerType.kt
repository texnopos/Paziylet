package com.example.recyclerviewyoutubeplayer.youtubeView.models


import androidx.annotation.IntDef

@IntDef(
    YouTubePlayerType.INVALID_VIEW,
    YouTubePlayerType.AUTO,
    YouTubePlayerType.STRICT_NATIVE,
    YouTubePlayerType.WEB_VIEW
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class YouTubePlayerType {
    companion object {
        /**
         * Invalid view
         */
       const val INVALID_VIEW = 0

        /**
         * Auto. If youtube service is available, it will render native player
         * else will fallback to WebView if web-url provided
         */
        const  val AUTO = 1

        /**
         * render only native player
         */
        const  val STRICT_NATIVE = 2

        /**
         * render only WebView player
         */
        const  val WEB_VIEW = 3
    }
}
