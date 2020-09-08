package com.dk.trellassignment.features.main

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import com.dk.trellassignment.R
import com.dk.trellassignment.coreplayer.PlayHelper
import com.dk.trellassignment.data.VideoItems
import com.dk.trellassignment.databinding.ActivityMainBinding
import com.dk.trellassignment.manager.MediaManager
import com.dk.trellassignment.manager.PlayerState
import com.dk.trellassignment.utils.SnapHelperExt
import com.dk.trellassignment.utils.SnapOnScrollListener
import com.google.android.exoplayer2.ui.PlayerView
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), VideoSelectionListener, PlayHelper.ServiceCallback,
    SnapOnScrollListener.OnSnapPositionChangeListener {

    private val TAG = "magic-MainActivity"
    private val viewModel : MainViewModel by inject()
    private val snapHelper : PagerSnapHelper by inject()
    private var mainBinding : ActivityMainBinding? = null
    private lateinit var mediaManager: MediaManager
    private var videoAdapter : VideoAdapter?= null
    private var videos : List<VideoItems>? =null
    private val PERMISSION : Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initViewModelObserver()
        requestPermission()
        initUi()
        mediaManager = MediaManager(this)
    }

    private fun requestPermission() {
        val result = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
        if(result ==  PERMISSION_GRANTED)
            viewModel.getData()
        else{
            ActivityCompat.requestPermissions(this,PERMISSION, 101)
        }
    }

    private fun initViewModelObserver() {
        viewModel.videosLiveData.observe(this, Observer {
            updateViewWithData(it)
        })
    }

    fun initUi(){
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding?.mainviewModel = viewModel
        snapHelper.attachToRecyclerView(mainBinding?.playerRecyclerView)

        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        mainBinding?.playerRecyclerView?.setHasFixedSize(false)
//        mainBinding?.playerRecyclerView?.setItemViewCacheSize(0)
        val snapOnScrollListener = SnapOnScrollListener(snapHelper, SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
            this)
        mainBinding?.playerRecyclerView?.addOnScrollListener(snapOnScrollListener)
        mainBinding?.playerRecyclerView?.layoutManager = layoutManager

        videoAdapter = VideoAdapter(this)
        mainBinding?.playerRecyclerView?.adapter = videoAdapter
    }

    private fun updateViewWithData(list: List<VideoItems>?) {
        this.videos = list
        videoAdapter?.updateItems(list)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 101 && grantResults[0] == PERMISSION_GRANTED){
            viewModel.getData()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onVideoSelected(items: VideoItems, playerView: PlayerView) {

//        mediaManager.play(items,playerView)
        Log.i(TAG,items.toString())
    }

    override fun onPlaybackStateChanged(state: PlayerState, position: Long) {
        Log.i(TAG,state.toString())
    }

    override fun onPlaybackMediaChanged(item: VideoItems?) {
        Log.i(TAG,item?.url.toString())
    }

    override fun onSnapPositionChange(position: Int) {
        Log.i(TAG,"position "+position)
//        videoAdapter?.notifyItemChanged(position)
         val playerView = videoAdapter?.map?.get(position)
        playerView?.player?.let { mediaManager.play(videos?.get(position), it) }

    }


}