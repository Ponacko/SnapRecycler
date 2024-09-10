package com.example.snaprecycler

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView


class GridLayoutManager(
    private val numRows: Int,
    private val numColumns: Int,
    private val context: Context
) : RecyclerView.LayoutManager() {

    private var horizontalScrollOffset = -1

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount == 0) {
            return
        }
        initializeHorizontalOffset()
        detachAndScrapAttachedViews(recycler);
        val itemsPerPage = getItemsPerPage()
        val pageWidth = width
        val itemWidth = pageWidth / numColumns
        val itemHeight = height / numRows

        for (position in 0 until itemCount) {
            val pageIndex = position / itemsPerPage
            val indexInPage = position % itemsPerPage
            val columnIndex = indexInPage % numColumns
            val rowIndex = indexInPage / numColumns

            val leftOffset: Int = if (isRTL()) {
                // In RTL, start from the right and subtract the width
                (getNumberOfPages() - pageIndex) * pageWidth - (columnIndex + 1) * itemWidth - horizontalScrollOffset
            } else {
                pageIndex * pageWidth + columnIndex * itemWidth + horizontalScrollOffset
            }
            val topOffset = rowIndex * itemHeight

            val view = recycler.getViewForPosition(position)
            addView(view)

            measureChildWithMargins(view, 0, 0)
            layoutDecorated(view, leftOffset, topOffset, leftOffset + itemWidth, topOffset + itemHeight)
        }
    }

    private fun initializeHorizontalOffset() {
        if (horizontalScrollOffset == -1) {
            horizontalScrollOffset = if (isRTL()) {
                // start of the last page
                getTotalWidth() - width
            } else {
                0
            }
        }
    }

    override fun canScrollHorizontally(): Boolean = true

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val delta = scrollByInternal(dx)
        offsetChildrenHorizontal(-delta)
        return delta
    }

    override fun scrollToPosition(position: Int) {
        val previousOffset = horizontalScrollOffset
        horizontalScrollOffset = position
        offsetChildrenHorizontal(previousOffset - horizontalScrollOffset)
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val itemsPerPage = getItemsPerPage()
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
                val targetPage = targetPosition / itemsPerPage
                val startOfTargetPage = targetPage * width
                return horizontalScrollOffset - startOfTargetPage
            }
        }
        val firstPositionOnTargetPage = position / itemsPerPage * itemsPerPage
        smoothScroller.targetPosition = firstPositionOnTargetPage
        startSmoothScroll(smoothScroller)
    }

    // This method fixes a bug where the RecyclerView would scroll away when item is removed
    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsRemoved(recyclerView, positionStart, itemCount)
        horizontalScrollOffset = 0
    }

    fun smoothScrollToPage(recyclerView: RecyclerView, page: Int) {
        val itemsPerPage = getItemsPerPage()
        val position = page * itemsPerPage
        recyclerView.post {
            smoothScrollToPosition(recyclerView, null, position)
        }
    }

    private fun scrollByInternal(dx: Int): Int {
        var delta = dx
        val totalWidth = getTotalWidth()

        if (horizontalScrollOffset + delta < 0) {
            delta = -horizontalScrollOffset
        } else if (horizontalScrollOffset + delta > totalWidth - width) {
            delta = totalWidth - width - horizontalScrollOffset
        }

        horizontalScrollOffset += delta
        return delta
    }

    // remainder results in an additional page
    private fun getNumberOfPages(): Int {
        val itemsPerPage = getItemsPerPage()
        return (itemCount + itemsPerPage - 1) / itemsPerPage
    }

    private fun getTotalWidth(): Int = width * getNumberOfPages()

    private fun getItemsPerPage() = numRows * numColumns

    private fun isRTL() = layoutDirection == View.LAYOUT_DIRECTION_RTL
}