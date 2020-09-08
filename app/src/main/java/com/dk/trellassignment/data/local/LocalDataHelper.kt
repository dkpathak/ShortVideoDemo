package com.dk.trellassignment.data.local

import android.provider.MediaStore
import com.dk.trellassignment.data.VideoItems

interface LocalDataHelper {

    suspend fun getVideosFromDevice() : List<VideoItems>
}