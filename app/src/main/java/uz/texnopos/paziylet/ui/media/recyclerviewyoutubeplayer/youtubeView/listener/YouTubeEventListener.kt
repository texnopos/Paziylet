package uz.texnopos.paziylet.ui.media.recyclerviewyoutubeplayer.youtubeView.listener


import androidx.annotation.MainThread

interface YouTubeEventListener {

    /**
     * fired when youtube player is ready
     */
    @MainThread
    fun onReady()

    /**
     * when video is playing.
     *
     * @param currentTime currentTime of seek-bar
     */
    @MainThread
    fun onPlay(currentTime: Int)

    /**
     * when video has been paused.
     *
     * @param currentTime paused time
     */
    @MainThread
    fun onPause(currentTime: Int)

    /**
     * when video has been stopped
     *
     * @param currentTime   stop time
     * @param totalDuration total duration of video
     */
    @MainThread
    fun onStop(currentTime: Int, totalDuration: Int)

    /**
     * when video is buffering
     *
     * @param currentTime current buffering time
     * @param isBuffering is video being buffered
     */
    @MainThread
    fun onBuffering(currentTime: Int, isBuffering: Boolean)

    /**
     * when seek-bar is moved
     *
     * @param currentTime       time from where seek-bar has been moved
     * @param newPositionMillis new position moved
     */
    @MainThread
    fun onSeekTo(currentTime: Int, newPositionMillis: Int)

    /**
     * when youtube player fails to initialize
     *
     * @param error message
     */
    @MainThread
    fun onInitializationFailure(error: String)

    /**
     * when native player is not supported
     */
    @MainThread
    fun onNativeNotSupported()

    /**
     * when video is cued
     */
    @MainThread
    fun onCued()
}
