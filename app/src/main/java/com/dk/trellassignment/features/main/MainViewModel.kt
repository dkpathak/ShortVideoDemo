package com.dk.trellassignment.features.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.trellassignment.data.DataHelper
import com.dk.trellassignment.data.VideoItems
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val dataHelper: DataHelper) : ViewModel() {

     val videosLiveData = MutableLiveData<List<VideoItems>>()

    fun getData(){
        viewModelScope.launch {
           val videos = dataHelper.getVideos()
            videosLiveData.value = videos

        }

    }

}