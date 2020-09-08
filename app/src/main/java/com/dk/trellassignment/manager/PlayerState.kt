package com.dk.trellassignment.manager

import com.dk.trellassignment.data.VideoItems


sealed class PlayerState {

    object Idle : PlayerState()
    object Stopped : PlayerState()
    data class Buffering(var item: VideoItems?) : PlayerState()
    data class Playing(var item: VideoItems?) : PlayerState()
    data class Paused(var item: VideoItems?) : PlayerState()
    data class Completed(var item: VideoItems?) : PlayerState()

    companion object {
        fun idle(): PlayerState =
            Idle

        fun stopped(): PlayerState =
            Stopped

        fun buffering(item: VideoItems?): PlayerState =
            Buffering(item)

        fun playing(item: VideoItems?): PlayerState =
            Playing(item)

        fun paused(item: VideoItems?): PlayerState =
            Paused(item)

        fun completed(item: VideoItems?): PlayerState =
            Completed(item)
    }
}