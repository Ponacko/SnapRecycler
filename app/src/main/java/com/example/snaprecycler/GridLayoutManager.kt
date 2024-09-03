package com.example.snaprecycler

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
        val totalWidth = width
        val totalHeight = height
        val itemWidth = totalWidth / numColumns
        val itemHeight = totalHeight / numRows

        var leftOffset = 0
        var topOffset = 0

        for (position in 0 until itemCount) {
            val view = recycler.getViewForPosition(position)
            addView(view)

            val left = leftOffset
            val right = left + width
            val top = topOffset
            val bottom = top + itemHeight
            measureChildWithMargins(view, 0, 0)
            layoutDecorated(view, left, top, right, bottom)

            // calculate new offsets
            leftOffset += itemWidth
            if ((position + 1) % numColumns == 0) {
                leftOffset = 0
                topOffset += itemHeight
            }
        }
    }

}