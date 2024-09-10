package com.example.snaprecycler

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlin.math.abs


class PageSnapHelper(
    private val pageSize: Int
) : LinearSnapHelper() {

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        val gridAdapter = recyclerView?.adapter as GridAdapter
        gridAdapter.setOnDragListener(DragListener()
        { snapToNearestPage(recyclerView) })
    }

    override fun findSnapView(layoutManager: LayoutManager): View? {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(
                layoutManager,
                OrientationHelper.createVerticalHelper(layoutManager)
            )
        } else if (layoutManager.canScrollHorizontally()) {
            return findCenterView(
                layoutManager,
                OrientationHelper.createHorizontalHelper(layoutManager)
            )
        }
        return null
    }

    private fun findCenterView(
        layoutManager: LayoutManager,
        helper: OrientationHelper
    ): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }

        var closestPage: View? = null
        val center = helper.startAfterPadding + helper.totalSpace / 2
        var absClosest = Int.MAX_VALUE
        val pages = childCount / pageSize + 1

        for (i in 0 until pages) {
            val index = i * pageSize
            if (index < childCount) {
                val pageStartPosition = layoutManager.getChildAt(index)
                val childCenter = (helper.getDecoratedStart(pageStartPosition)
                            + (helper.getDecoratedMeasurement(pageStartPosition) / 2))

                val absDistance = abs((childCenter - center).toDouble()).toInt()

                if (absDistance < absClosest) {
                    absClosest = absDistance
                    closestPage = pageStartPosition
                }
            }
        }
        return closestPage
    }

    fun snapToNearestPage(recyclerView: RecyclerView?) {
        val layoutManager = recyclerView?.layoutManager ?: return
        val snapView = findSnapView(layoutManager)
        if (snapView != null) {
            val position = recyclerView.getChildAdapterPosition(snapView)
            recyclerView.smoothScrollToPosition(position)
        }
    }
}