package com.dk.trellassignment.coreplayer

import android.net.Uri
import com.dk.trellassignment.data.VideoItems
import com.dk.trellassignment.manager.PlayerState
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView

interface PlayHelper {

    /* The base interface for any playback implementation */
    interface PlayerCallback {


        val isPlaying: Boolean

        val position: Long

        val duration: Long

        fun prepareMediaSources(items: List<VideoItems>) : List<MediaSource>

        fun play(item: VideoItems?, playerView: PlayerView?)

        fun pause()

        fun complete()

        fun stop()

        fun setCallback(callback: ManagerCallback)

        fun invalidateCurrent()

    }

    /* This interface is used to notify MediaPlaybackManager about changes in the player*/
    interface ManagerCallback {

        fun onBuffer()

        fun onPlay()

        fun onPause()

        fun onCompletion()

        fun onIdle()

        fun onError()

    }

    /* This interface is used to notify Foreground Service about changes in the player's state*/
    interface ServiceCallback {

        fun onPlaybackStateChanged(state: PlayerState, position: Long)

        fun onPlaybackMediaChanged(item: VideoItems?)

    }

}