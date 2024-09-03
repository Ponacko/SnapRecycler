package com.example.snaprecycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GridLayoutManager(
    private val numRows: Int,
    private val numColumns: Int
) : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount == 0) {
            return
        }
        val itemsPerPage = numRows * numColumns
        val pages = (itemCount + itemsPerPage - 1) / itemsPerPage // remainder results in an additional grid
        val pageWidth = width / pages
        val itemWidth = pageWidth / numColumns
        val itemHeight = height / numRows

        for (position in 0 until itemCount) {
            val gridIndex = position / itemsPerPage
            val indexInGrid = position % itemsPerPage
            val columnIndex = indexInGrid % numColumns
            val rowIndex = indexInGrid / numColumns

            val leftOffset: Int = if (isRTL()) {
                // In RTL, start from the right and subtract the width
                (pages - gridIndex) * pageWidth - (columnIndex + 1) * itemWidth
            } else {
                gridIndex * pageWidth + columnIndex * itemWidth
            }
            val topOffset = rowIndex * itemHeight

            val view = recycler.getViewForPosition(position)
            addView(view)

            measureChildWithMargins(view, 0, 0)
            layoutDecorated(view, leftOffset, topOffset, leftOffset + itemWidth, topOffset + itemHeight)
        }
    }

    private fun isRTL(): Boolean {
        val isRTL = layoutDirection == View.LAYOUT_DIRECTION_RTL
        return isRTL
    }

}