package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.models


import androidx.annotation.StringDef

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

object PlayerStateList {
    const val NOT_STARTED = "NOT_STARTED"
    const val ENDED = "ENDED"
    const val PLAYING = "PLAYING"
    const val PAUSED = "PAUSED"
    const val BUFFERING = "BUFFERING"
    const val CUED = "CUED"
    const val NONE = "NONE"
    const val STOPPED = "STOPPED"

    @StringDef(NOT_STARTED, ENDED, PLAYING, PAUSED, BUFFERING, CUED, NONE, STOPPED)
    @Retention(RetentionPolicy.SOURCE)
    annotation class PlayerState
}