package com.dk.trellassignment.data

import android.app.Application
import com.dk.trellassignment.data.local.LocalDataHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataManager(val localDataHelper: LocalDataHelper,  val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : DataHelper {

    override suspend fun getVideos(): List<VideoItems> {
       return withContext(ioDispatcher){
           localDataHelper.getVideosFromDevice()
       }
    }


}