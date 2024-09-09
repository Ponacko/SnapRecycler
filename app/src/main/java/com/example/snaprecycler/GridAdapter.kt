package com.example.snaprecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GridAdapter(
    private val itemList: MutableList<GridItem>
) : RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textView.text = currentItem.name
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val movedItem = itemList.removeAt(fromPosition)
        itemList.add(toPosition, movedItem)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun getItemCount(): Int = itemList.size
}