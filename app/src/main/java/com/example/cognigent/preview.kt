package com.example.cognigent

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class preview : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var questionList: List<QuestionModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        val back = findViewById<Button>(R.id.backBtn)
        back.setOnClickListener {
            finish()
        }
          var array: IntArray
         array = intent.getIntArrayExtra("arr")?:IntArray(0)

        recyclerView = findViewById(R.id.reviewRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // ✅ 3 columns
        recyclerView.adapter = PreviewAdapter(  this,array)
    }
}
