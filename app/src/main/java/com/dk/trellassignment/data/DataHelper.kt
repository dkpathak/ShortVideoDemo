package com.dk.trellassignment.data

import android.provider.MediaStore

interface DataHelper {

    suspend fun getVideos() : List<VideoItems>
}