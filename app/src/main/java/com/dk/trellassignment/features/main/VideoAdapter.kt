package com.dk.trellassignment.features.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dk.trellassignment.R
import com.dk.trellassignment.data.VideoItems
import com.dk.trellassignment.databinding.VideoViewItemBinding
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.android.synthetic.main.video_view_item.view.*
import java.util.zip.Inflater

class VideoAdapter(private val videoSelectionListener: VideoSelectionListener
                   ) : RecyclerView.Adapter<VideoVieHolder>() {

    private var videos : List<VideoItems>?=null
    private val TAG = "Magic-VideoAdapter"

    var map = HashMap<Int,VideoVieHolder>()

    fun updateItems( videos : List<VideoItems>?){
        this.videos = videos;
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVieHolder {
        Log.i(TAG,"onCreateViewHolder - " )
        val videoViewItemBinding = DataBindingUtil.inflate<VideoViewItemBinding>(LayoutInflater.from(parent.context),
            R.layout.video_view_item, parent,false)
               return VideoVieHolder(videoViewItemBinding.root)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        Log.i(TAG,"onAttachedToRecyclerView")
    }

    override fun onViewAttachedToWindow(holder: VideoVieHolder) {
        super.onViewAttachedToWindow(holder)
        Log.i(TAG,"onViewAttachedToWindow " +holder.oldPosition +"<-Old"+holder.toString())
    }

    override fun onViewDetachedFromWindow(holder: VideoVieHolder) {
        Log.i(TAG,"onViewDetachedFromWindow " +holder.oldPosition +"<-Old"+holder.toString())
        super.onViewDetachedFromWindow(holder)
    }
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        Log.i(TAG,"onDetachedFromRecyclerView")
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onBindViewHolder(holder: VideoVieHolder, position: Int) {
        Log.i(TAG,"onBindViewHolder - "+position )
        holder.setIsRecyclable(false)
        videos?.get(position)?.let {
            map.put(position,holder)
        }
    }

    override fun onViewRecycled(holder: VideoVieHolder) {
        Log.i(TAG,"onViewRecycled")
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return videos?.size?:0
    }

}

    class VideoVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val player: PlayerView = itemView.player_view
    }
