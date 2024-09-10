package com.example.snaprecycler

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numRows = 3
        val numColumns = 3
        // Sample data
        val itemList = getItemList(15)
        val recyclerView = setupRecycler(itemList, R.id.recyclerView, numRows, numColumns)

        val numRows2 = 4
        val numColumns2 = 2
        val itemList2 = getItemList(9)
        val recyclerView2 = setupRecycler(itemList2, R.id.recyclerView2, numRows2, numColumns2)
    }

    private fun createPageSnapHelper(
        numRows: Int,
        numColumns: Int
    ) = PageSnapHelper(numRows * numColumns)

    private fun createGridLayoutManager(
        numRows: Int,
        numColumns: Int
    ): GridLayoutManager = GridLayoutManager(numRows, numColumns, this)

    private fun getItemList(items: Int): MutableList<GridItem> =
        MutableList(items) { GridItem("Item ${it + 1}") }

    private fun setupRecycler(
        itemList: MutableList<GridItem>,
        id: Int,
        numRows: Int,
        numColumns: Int
    ): RecyclerView? {
        val recyclerView = findViewById<RecyclerView>(id)

        val layoutManager = createGridLayoutManager(numRows, numColumns)
        recyclerView.layoutManager = layoutManager

        val adapter = GridAdapter(itemList)
        recyclerView.adapter = adapter

        val snapHelper = createPageSnapHelper(numRows, numColumns)
        snapHelper.attachToRecyclerView(recyclerView)

        val itemTouchHelperCallback = ItemMoveCallback(adapter, snapHelper)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return recyclerView
    }
}