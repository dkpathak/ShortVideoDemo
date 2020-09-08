package com.dk.trellassignment.utils
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
class SnapHelperExt {

    fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }
}