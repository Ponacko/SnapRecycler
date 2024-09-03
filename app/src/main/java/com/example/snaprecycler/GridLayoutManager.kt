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
        val itemsPerGrid = numRows * numColumns
        val grids = (itemCount + itemsPerGrid - 1) / itemsPerGrid // remainder results in an additional grid
        val gridWidth = width / grids
        val gridHeight = height
        val itemWidth = gridWidth / numColumns
        val itemHeight = gridHeight / numRows

        for (position in 0 until itemCount) {
            val gridIndex = position / itemsPerGrid
            val indexInGrid = position % itemsPerGrid
            val columnIndex = indexInGrid % numColumns
            val rowIndex = indexInGrid / numColumns

            val leftOffset = gridIndex * gridWidth + columnIndex * itemWidth
            val topOffset = rowIndex * itemHeight

            val view = recycler.getViewForPosition(position)
            addView(view)

            measureChildWithMargins(view, 0, 0)
            layoutDecorated(view, leftOffset, topOffset, leftOffset + itemWidth, topOffset + itemHeight)
        }
    }

}