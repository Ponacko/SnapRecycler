package com.example.snaprecycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(2, 5)

        // Sample data
        val itemList = List(20) { GridItem("Item ${it + 1}") }
        val adapter = GridAdapter(itemList)
        recyclerView.adapter = adapter
    }
}