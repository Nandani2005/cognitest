package com.example.cognigent

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class testPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_page)

        // Reference to ListView
        val listView = findViewById<ListView>(R.id.list)
        // Data to show in the ListView
        val dataList = listOf(
            "Math Test",
            "Science Quiz",
            "English Exam",
            "History Paper",
            "Geography Assignment",
            "Coding Challenge"
        )

        // Create adapter
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            dataList
        )

        // Set adapter to ListView
        listView.adapter = adapter
    }
}