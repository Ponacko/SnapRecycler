package com.example.snaprecycler

import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class DragListener internal constructor(
    private val onDropAction: () -> Unit
) : View.OnDragListener {
    private var isDropped = false
    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DROP -> {
                isDropped = true
                var positionTarget = -1
                val viewSource = event.localState as View?
                val viewId = v.id
                val listItem = R.id.item_grid
                val recyclerId1 = R.id.recyclerView
                val recyclerId2 = R.id.recyclerView2
                when (viewId) {
                    listItem, recyclerId1, recyclerId2 -> {
                        val target: RecyclerView
                        when (viewId) {
                            recyclerId1 -> target = v.rootView.findViewById<View>(recyclerId1) as RecyclerView
                            recyclerId2 -> target = v.rootView.findViewById<View>(recyclerId2) as RecyclerView
                            else -> {
                                target = v.parent as RecyclerView
                                positionTarget = (v.tag ?: -1) as Int
                            }
                        }
                        if (viewSource != null) {
                            val source = viewSource.parent as RecyclerView
                            val adapterSource = source.adapter as GridAdapter?
                            val positionSource = viewSource.tag as Int
                            val removedItem = adapterSource?.removeItem(positionSource)
                            val adapterTarget = target.adapter as GridAdapter?
                            if (positionTarget >= 0) {
                                removedItem?.let { adapterTarget?.addItem(it, positionTarget) }
                            } else {
                                removedItem?.let { adapterTarget?.addItem(it) }
                            }
                            onDropAction()
                        }
                    }
                }
            }
        }
        if (!isDropped && event.localState != null) {
            (event.localState as View).visibility = View.VISIBLE
        }
        return true
    }
}