package com.dk.trellassignment.features.main

import com.dk.trellassignment.data.VideoItems
import com.google.android.exoplayer2.ui.PlayerView

interface VideoSelectionListener {

    fun onVideoSelected(items: VideoItems, playerView: PlayerView)
}