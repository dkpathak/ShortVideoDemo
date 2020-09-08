package com.dk.trellassignment.coreplayer

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.util.Log
import com.dk.trellassignment.data.VideoItems

import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.Util
import org.koin.core.KoinComponent
import org.koin.core.inject

internal class TrellMediaPlayer(
    context: Context,
    audioManager: AudioManager,
    wifiLock: WifiManager.WifiLock
) : BasePlayer(context, audioManager, wifiLock), Player.EventListener,PlaybackPreparer, KoinComponent {

    private val dataSourceFactory: DataSource.Factory by inject()
    private val player : SimpleExoPlayer by inject()
     private val TAG = "magic-TrellMediaPlayer"


    override val isPlaying: Boolean
        get() = player.playWhenReady

    override val position: Long
        get() = player.currentPosition

    override val duration: Long
        get() = player.duration

    override fun prepareMediaSources(items: List<VideoItems>): List<MediaSource> {
        TODO("Not yet implemented")
    }

    override fun startPlayer(playerView: PlayerView) {
        Log.i(TAG, "startPlayer")
        play(playerView)
    }

    override fun pausePlayer() {
        Log.i(TAG, "pausePlayer")
        player.playWhenReady = false
    }

    override fun stopPlayer() {
        Log.i(TAG, "stopPlayer")
        player.release()
        player.removeListener(this)
        player.playWhenReady = false

    }

    override fun resumePlayer() {
        Log.i(TAG, "resumePlayer")
        player.playWhenReady = true
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> playbackCallback?.onIdle()
            Player.STATE_READY -> if (playWhenReady) playbackCallback?.onPlay() else playbackCallback?.onPause()
            Player.STATE_BUFFERING -> if(playWhenReady) playbackCallback?.onBuffer() else playbackCallback?.onPause()
            Player.STATE_ENDED -> playbackCallback?.onCompletion()
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        Log.i(TAG, "onPlayerError")
        super.playbackCallback?.onError()
    }


    private fun play(playerView: PlayerView) { // Last method call to play
        Log.i(TAG, "play")
        val mediaSource = currentMedia?.let { createPlayerMediaSource(it) }
        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL;
        playerView.player = player
        playerView.setPlaybackPreparer(this)
        player.addListener(this)
        mediaSource?.let { player.prepare(it) }
        player.playWhenReady = true

    }

    private fun createPlayerMediaSource(trackData: VideoItems): MediaSource? {

        return createLeafMediaSource(trackData.url)
    }

    private fun createLeafMediaSource(
        uri: Uri): MediaSource? {
        @C.ContentType val type =
            Util.inferContentType(uri)

        return when (type) {
            C.TYPE_DASH -> DashMediaSource.Factory(
                dataSourceFactory
            ).createMediaSource(uri)

            C.TYPE_HLS -> HlsMediaSource.Factory(
                dataSourceFactory
            ).createMediaSource(uri)

            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(
                dataSourceFactory
            ).createMediaSource(uri)

            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }

    override fun preparePlayback() {
        player.retry();
    }
}