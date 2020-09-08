package com.dk.trellassignment.manager

import android.util.Log
import com.dk.trellassignment.coreplayer.PlayHelper
import com.dk.trellassignment.data.VideoItems
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import org.koin.core.KoinComponent
import org.koin.core.inject

class MediaManager(val serviceCallback: PlayHelper.ServiceCallback) : PlayHelper.ManagerCallback, KoinComponent{

    private val playerCallback : PlayHelper.PlayerCallback by inject()
    private var item : VideoItems?=null
    private val  sources = arrayListOf<MediaSource>()
    private val TAG = "MediaManager"
    init {
        playerCallback.setCallback(this)
    }

    override fun onBuffer() {
        updatePlayer(PlayerState.buffering(item), playerCallback.position)

    }

    override fun onPlay() {
        updatePlayer(PlayerState.playing(item), playerCallback.position)
    }

    override fun onPause() {
        updatePlayer(PlayerState.paused(item), playerCallback.position)
    }

    override fun onCompletion() {
        updatePlayer(PlayerState.completed(item), playerCallback.position)
    }

    override fun onIdle() {
        updatePlayer(PlayerState.idle(), playerCallback.position)
    }

    override fun onError() {
        updatePlayer(PlayerState.stopped(), playerCallback.position)
    }

    fun play(item: VideoItems?, playerView: PlayerView) {
        Log.i(TAG, "play"+item?.name)
        playerCallback.play(item,playerView)
        serviceCallback.onPlaybackMediaChanged(item)
        updatePlayer(PlayerState.playing(item), playerCallback.position)
    }

    private fun updatePlayer(playerState: PlayerState, position: Long){
        serviceCallback.onPlaybackStateChanged(playerState,position)
    }


}