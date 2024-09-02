package com.example.snaprecycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GridLayoutManager(
    private val numRows: Int,
    private val numColumns: Int
) : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


}