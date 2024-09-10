package com.example.snaprecycler

import android.content.ClipData
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GridAdapter(
    private val itemList: MutableList<GridItem>
) : RecyclerView.Adapter<GridAdapter.GridViewHolder>(), View.OnLongClickListener {

    private var dragListener: DragListener? = null

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemTextView)
        val layout: LinearLayout = itemView.findViewById(R.id.item_grid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textView.text = currentItem.name
        holder.layout.setOnLongClickListener(this)
        holder.layout.setOnDragListener(dragListener)
        holder.layout.tag = position
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val movedItem = itemList.removeAt(fromPosition)
        itemList.add(toPosition, movedItem)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun addItem(item: GridItem) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    fun addItem(item: GridItem, position: Int) {
        itemList.add(position, item)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int): GridItem {
        val removed = itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemList.size)
        return removed
    }

    override fun getItemCount(): Int = itemList.size

    override fun onLongClick(view: View?): Boolean {
        val data = ClipData.newPlainText("", "")
        val shadowBuilder = View.DragShadowBuilder(view)
        view?.startDragAndDrop(data, shadowBuilder, view, 0)
        return true
    }

    fun setOnDragListener(dragListener: DragListener) {
        this.dragListener = dragListener
    }
}