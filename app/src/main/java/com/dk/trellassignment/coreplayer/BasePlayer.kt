package com.dk.trellassignment.coreplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.util.Log
import com.dk.trellassignment.data.VideoItems
import com.google.android.exoplayer2.ui.PlayerView

internal abstract class BasePlayer(
    protected val context: Context, private val audioManager: AudioManager,
    private val wifiLock: WifiManager.WifiLock
) : PlayHelper.PlayerCallback, AudioManager.OnAudioFocusChangeListener {
    private val TAG = "magic-BasePlayer"
    protected var playbackCallback: PlayHelper.ManagerCallback? = null

    @Volatile
    protected var currentMedia: VideoItems? = null

    @Volatile
    protected var currentPlayerView: PlayerView? = null

    private var receiverRegistered = false
    private val audioBecomingNoisyIntent = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

    private val audioNoisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                pause()
            }
        }
    }

    abstract fun startPlayer(playerView: PlayerView)

    abstract fun pausePlayer()

    abstract fun resumePlayer()

    abstract fun stopPlayer()

    override fun play(item: VideoItems?, playerView: PlayerView?) {
        Log.i(TAG, "play")
        if (item != null) {
            requestFocus()
            registerWifiLock()
            registerNoiseReceiver()
//            if (item == currentMedia) {
//                resumePlayer()
//            } else {
                currentMedia = item
                currentPlayerView = playerView
                if (playerView != null) {
                    startPlayer(playerView)
//                }
            }
        }
    }

    override fun invalidateCurrent() {
        currentMedia = null
        currentPlayerView = null
    }

    override fun pause() {
        pausePlayer()
        unregisterWifiLock()
        unregisterNoiseReceiver()
    }

    override fun complete() {
        invalidateCurrent()
        unregisterWifiLock()
        unregisterNoiseReceiver()
    }

    override fun stop() {
        releaseFocus()
        unregisterWifiLock()
        unregisterNoiseReceiver()
        stopPlayer()
        invalidateCurrent()
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                play(currentMedia,currentPlayerView)
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> if (isPlaying) {
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS -> if (isPlaying) {
                pause()
            }
        }
    }

    override fun setCallback(callback: PlayHelper.ManagerCallback) {
        playbackCallback = callback
    }

    private fun requestFocus() {
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
    }

    private fun releaseFocus() {
        audioManager.abandonAudioFocus(this)
    }

    private fun registerWifiLock() {
        if (!wifiLock.isHeld) {
            wifiLock.acquire()
        }
    }

    private fun unregisterWifiLock() {
        if (wifiLock.isHeld) {
            wifiLock.release()
        }
    }

    private fun registerNoiseReceiver() {
        if(!receiverRegistered) {
            context.registerReceiver(audioNoisyReceiver, audioBecomingNoisyIntent)
            receiverRegistered = true
        }
    }

    private fun unregisterNoiseReceiver() {
        if(receiverRegistered) {
            context.unregisterReceiver(audioNoisyReceiver)
            receiverRegistered = false
        }
    }

}